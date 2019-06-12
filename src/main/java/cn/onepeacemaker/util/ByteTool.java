package cn.onepeacemaker.util;

/**
 * Created by ouyang on 2017/2/13.
 */
public class ByteTool {
    /*
    看是正常的BOM还是反转的BOM
     */
    public static boolean handleByteBom(String tag,byte[] bytes){

        if(bytes[0]==-1&&bytes[1]==-2){
            return true;
        }else if(bytes[0]==-2&&bytes[1]==-1){
            return false;
        }
        return false;
    }
    public static String getByteInString(byte[] bytes){
        return new String(bytes);
    }
    public static String getByteInString(byte[] bytes,String charsetName,boolean isReverse){
        String text = "";
        try {
            if(isReverse){
                int size = bytes.length/2;
                byte temp;
                for (int i = 0; i < size; i++) {
                    temp = bytes[2*i];
                    bytes[2*i] = bytes[2*i+1];
                    bytes[2*i+1] = temp;
                }
            }
            text = new String(bytes,charsetName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return text;
    }
}
