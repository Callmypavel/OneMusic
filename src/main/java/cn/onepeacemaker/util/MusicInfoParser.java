package cn.onepeacemaker.util;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Created by ouyang on 2017/2/10.
 */
public class MusicInfoParser {
    private static LinkedList<File> directorylist = new LinkedList<>();
    private static LinkedList<File> filelist = new LinkedList<>();
    public interface OnMusicInfoListener{
        void onMusicInfoLoaded(String url, String singerName, String songName, String albumName, String trackPosition, long size);
        void onLoadFinished();
    }
    private static OnMusicInfoListener listener;
    public static void setOnMusicInfoListener(OnMusicInfoListener listener){
        MusicInfoParser.listener = listener;
    }
    public static void parseMusicInfos(String url,OnMusicInfoListener onMusicInfoListener){
        setOnMusicInfoListener(onMusicInfoListener);
        updateMusicFiles(url);
        for (File file : filelist) {
            decodeMusicInfo(file,url);
        }
        listener.onLoadFinished();

    }
    public static void decodeMusicInfo(File file,String url){
        String singerName = "",songName = "unknown",albumName = "",trackPosition = "",fileName = file.getName();
        long totalSize = file.length();
        //LogTool.log("MusicInfoParser","获取歌曲信息:"+file.getName());
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[3];
            if(fis.read(bytes)!=-1){
                //LogTool.logByteInString("MusicInfoParser-->file identifier",bytes);
            }
            bytes = new byte[2];
            if(fis.read(bytes)!=-1){
                //LogTool.logByte("MusicInfoParser-->file version",bytes);
            }
            bytes = new byte[1];
            if(fis.read(bytes)!=-1){
                //LogTool.logByteInBit("MusicInfoParser-->flags",bytes);
            }
            bytes = new byte[4];
            if(fis.read(bytes)!=-1){
                int a = bytes[3]>=0?bytes[3]:bytes[3]+256;
                int b = bytes[2]>=0?bytes[2]:bytes[2]+256;
                int c = bytes[1]>=0?bytes[1]:bytes[1]+256;
                int d = bytes[0]>=0?bytes[0]:bytes[0]+256;
                int size = a+b*256+c*65536+d*16777216;
                //LogTool.log("MusicInfoParser-->size",size+"");
            }
            bytes = new byte[4];
            fis.read(bytes);
            String id = new String(bytes);
            while(checkId(id)!=-1){
                //LogTool.log("MusicInfoParser-->id",id);
                bytes = new byte[4];
                fis.read(bytes);
                //防止溢出
                int a = bytes[3]>=0?bytes[3]:bytes[3]+256;
                int b = bytes[2]>=0?bytes[2]:bytes[2]+256;
                int c = bytes[1]>=0?bytes[1]:bytes[1]+256;
                int d = bytes[0]>=0?bytes[0]:bytes[0]+256;
                int size = a+b*256+c*65536+d*16777216;
                //LogTool.log("MusicInfoParser-->check bytes",a+","+b+","+c+","+d);
                //LogTool.log("MusicInfoParser-->该帧长度：",size+"byte");
                bytes = new byte[2];
                fis.read(bytes);
                //LogTool.log("MusicInfoParser-->check the size",size+"byte");
                bytes = new byte[size];
                fis.read(bytes);
                if(id.startsWith("T")){
                    byte encoding = bytes[0];
                    //为1则为默认UNICODE编码，为0则为ISO-8859-1
                    //LogTool.log("MusicInfoParser-->encoding",""+encoding);
                    byte[] bomBytes = cutBytes(bytes,1,2);
                    boolean isReverse = ByteTool.handleByteBom("MusicInfoParser-->bom",bomBytes);
                    //FF FE两位是用来标识BOM的，无意义，所以直接跳到3
                    byte[] content = cutBytes(bytes,3,bytes.length-1);

                    String contentString = "";
                    if(encoding==1){
                        contentString = ByteTool.getByteInString(content,"UNICODE",isReverse);
                    }else{
                        contentString = ByteTool.getByteInString(content);
                    }
                    if(id.equals("TALB")){
                        albumName = contentString;
                    }else if(id.equals("TIT2")){
                        songName = contentString;
                    }else if(id.equals("TPE1")){
                        singerName = contentString;
                    }else if(id.equals("TRCK")){
                        trackPosition = contentString;
                    }

                }else if(id.equals("APIC")){
                    try{
                        ArrayList<Byte> byteArrayList = readUntil(0,bytes,1);
                        int typeStringSize = byteArrayList.size();
                        int end = typeStringSize+1;
                        byte[] type = new byte[typeStringSize];
                        for (int i = 0; i < typeStringSize; i++) {
                            type[i]=bytes[i+1];
                        }
                        String typeString = ByteTool.getByteInString(type);
                        int length = readUntil(0,bytes,end+1).size()+end+2 ;
                        length = length+readUntilNot(0,bytes,length).size();
                        //LogTool.log("MusicInfoParser-->图片头长度：",length+"bytes");
                        int pictureSize = size - length;
                        if(typeString.equals("image/jpeg")){
                            //LogTool.log("file name",file.getName());
                            //String filename = file.getName();
                            String pictureName = url+"/pictures/"+StringUtil.convertForFileName(albumName)+".jpg";
//                            int dot = filename.lastIndexOf('.');
//                            if ((dot >-1) && (dot < (filename.length()))) {
//                                pictureName = filename.substring(0, dot);
//                            }
                            //LogTool.log("检查图片目录",rootPath+"是否为空");
                            File file1 = new File(url+"/pictures/");
                            if(!file1.exists()){
                                file1.mkdirs();
                            }
                            FileOutputStream fos = new FileOutputStream(pictureName);
                            fos.write(bytes,length,pictureSize);
                            fos.flush();
                            fos.close();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                bytes = new byte[4];
                fis.read(bytes);
                id = new String(bytes);
            }
            listener.onMusicInfoLoaded(file.getPath(),singerName,songName,albumName,trackPosition,totalSize);


        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }
    private static byte[] cutBytes(byte[] bytes,int start,int end){
        int size = end-start+1;
        byte[] temp = new byte[size];
        for (int i = 0; i < size; i++) {
            temp[i]=bytes[start+i];
        }
        return temp;
    }
    private static ArrayList<Byte> readUntil(int endFlag,byte[] bytes,int begin)throws Exception{
        byte temp = bytes[begin];
        ArrayList<Byte> bytesArray = new ArrayList<>();
        while(temp!=endFlag){
            bytesArray.add(temp);
            begin+=1;
            temp = bytes[begin];
        }
        return bytesArray;
    }
    private static ArrayList<Byte> readUntilNot(int flag,byte[] bytes,int begin)throws Exception{
        byte temp = bytes[begin];
        ArrayList<Byte> bytesArray = new ArrayList<>();
        while(temp==flag){
            bytesArray.add(temp);
            begin+=1;
            temp = bytes[begin];
        }
        return bytesArray;
    }
    private static int checkId(String id){
        //LogTool.log("check the id",id);
        if(id.equals("AENC")){
            return 1;
        }else if(id.equals("APIC")){
            return 1;
        }else if(id.equals("COMM")){
            return 1;
        }else if(id.equals("COMR")){
            return 1;
        }else if(id.equals("ENCR")){
            return 1;
        }else if(id.equals("EQUA")){
            return 1;
        }else if(id.equals("ETCO")){
            return 1;
        }else if(id.equals("GEOB")){
            return 1;
        }else if(id.equals("GRID")){
            return 1;
        }else if(id.equals("IPLS")){
            return 1;
        }else if(id.equals("LINK")){
            return 1;
        }else if(id.equals("MCDI")){
            return 1;
        }else if(id.equals("MLLT")){
            return 1;
        }else if(id.equals("OWNE")){
            return 1;
        }else if(id.equals("PRIV")){
            return 1;
        }else if(id.equals("PCNT")){
            return 1;
        }else if(id.equals("POPM")){
            return 1;
        }else if(id.equals("POSS")){
            return 1;
        }else if(id.equals("RBUF")){
            return 1;
        }else if(id.equals("RVAD")){
            return 1;
        }else if(id.equals("RVRB")){
            return 1;
        }else if(id.equals("SYLT")){
            return 1;
        }else if(id.equals("SYTC")){
            return 1;
        }else if(id.equals("TALB")){
            return 1;
        }else if(id.equals("TBPM")){
            return 1;
        }else if(id.equals("TCOM")){
            return 1;
        }else if(id.equals("TCON")){
            return 1;
        }else if(id.equals("TCOP")){
            return 1;
        }else if(id.equals("TDAT")){
            return 1;
        }else if(id.equals("TDLY")){
            return 1;
        }else if(id.equals("TENC")){
            return 1;
        }else if(id.equals("TEXT")){
            return 1;
        }else if(id.equals("TFLT")){
            return 1;
        }else if(id.equals("TIME")){
            return 1;
        }else if(id.equals("TIT1")){
            return 1;
        }else if(id.equals("TIT2")){
            return 1;
        }else if(id.equals("TIT3")){
            return 1;
        }else if(id.equals("TKEY")){
            return 1;
        }else if(id.equals("TLAN")){
            return 1;
        }else if(id.equals("TMED")){
            return 1;
        }else if(id.equals("TOAL")){
            return 1;
        }else if(id.equals("TOFN")){
            return 1;
        }else if(id.equals("TORY")){
            return 1;
        }else if(id.equals("TOWN")){
            return 1;
        }else if(id.equals("TPE1")){
            return 1;
        }else if(id.equals("TPE2")){
            return 1;
        }else if(id.equals("TPE3")){
            return 1;
        }else if(id.equals("TPE4")){
            return 1;
        }else if(id.equals("TPOS")){
            return 1;
        }else if(id.equals("TPUB")){
            return 1;
        }else if(id.equals("TRCK")){
            return 1;
        }else if(id.equals("TRDA")){
            return 1;
        }else if(id.equals("TRSN")){
            return 1;
        }else if(id.equals("TRSO")){
            return 1;
        }else if(id.equals("TSIZ")){
            return 1;
        }else if(id.equals("TSRC")){
            return 1;
        }else if(id.equals("TSSE")){
            return 1;
        }else if(id.equals("TYER")){
            return 1;
        }else if(id.equals("TXXX")){
            return 1;
        }else if(id.equals("UFID")){
            return 1;
        }else if(id.equals("USER")){
            return 1;
        }else if(id.equals("USLT")){
            return 1;
        }else if(id.equals("WCOM")){
            return 1;
        }else if(id.equals("WCOP")){
            return 1;
        }else if(id.equals("WOAF")){
            return 1;
        }else if(id.equals("WOAR")){
            return 1;
        }else if(id.equals("WOAS")){
            return 1;
        }else if(id.equals("WORS")){
            return 1;
        }else if(id.equals("WPAY")){
            return 1;
        }else if(id.equals("WPUB")){
            return 1;
        }else if(id.equals("WXXX")){
            return 1;
        }
        return -1;
    }
    private static void updateMusicFiles(String url){
        //rootPath = rootPath.replace("\\","/");
        File[] files = new File(url).listFiles();
        //LogTool.log("MusicUpdater","查看路径"+rootPath+"/music");
        for (File file1 : files) {
            if (file1.isDirectory()) {
                directorylist.add(file1);
            } else{
                if (file1.getPath().endsWith(".mp3")) {
                    filelist.add(file1);
                }
            }
        }
        File tmp;
        while (!directorylist.isEmpty()) {
            tmp = directorylist.removeFirst();
            if (tmp.isDirectory()) {
                if (tmp.listFiles() == null)
                    continue;
                for (File file2 : tmp.listFiles()) {
                    if (file2.isDirectory()) {
                        directorylist.add(file2);
                    }else{
                        if (file2.getPath().endsWith(".mp3")) {
                            filelist.add(file2);
                        }
                    }
                }
            } else {
                if (tmp.getPath().endsWith(".mp3")) {
                    filelist.add(tmp);
                }
            }
        }
    }
}
