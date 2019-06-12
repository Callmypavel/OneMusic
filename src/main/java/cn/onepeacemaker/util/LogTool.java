package cn.onepeacemaker.util;

/**
 * Created by ouyan on 2016/9/22.
 */

public class LogTool {
    private static boolean isLogOpen = true;

    public static void log(String tag,String message){
        if(isLogOpen){
            System.out.println(tag+":"+message);
        }
    }
    public static void log(Object object,String message){
        if(isLogOpen){
            System.out.println(object.getClass().getSimpleName()+":--------------------------------------------------------->"+message);
        }
    }
    public static String logByteInString(String tag,byte[] bytes,String charsetName,boolean isReverse){
        String text = "";
        if(isLogOpen){
            text = ByteTool.getByteInString(bytes,charsetName,isReverse);
            System.out.println(tag+":"+text);
        }
        return text;
    }
    public static String logByteInString(String tag,byte[] bytes){
        String text = "";
        if(isLogOpen){
            try {
                text = ByteTool.getByteInString(bytes);
                System.out.println(tag+":"+text);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return text;
    }
    public static String logByteInString(String tag,Byte[] bytes){
        if(isLogOpen){

            System.out.println(tag+":"+bytes.toString());
        }
        return tag;
    }
    public static void logByteInChar(String tag,byte[] bytes){
        if(isLogOpen){
            for(byte b : bytes){
                System.out.println(tag+":"+(char)b);
            }

        }
    }
    public static void logByte(String tag,byte[] bytes){
        if(isLogOpen){
            for(byte b : bytes){
                System.out.println(tag+":"+b);
            }

        }
    }
    public static boolean logByteBom(String tag,byte[] bytes){
        if(isLogOpen){
            if(bytes[0]==-1&&bytes[1]==-2){
                return true;
            }else if(bytes[0]==-2&&bytes[1]==-1){
                return false;
            }

        }
        return false;
    }
    public static void logBytes(String tag,byte[] bytes){
        if(isLogOpen){
            String content = "";
            for(byte b : bytes){
                content+=b;
            }
            System.out.println(tag+":"+content);
        }
    }
    public static void logByteInBit(String tag,byte[] bytes){
        if(isLogOpen){
            for(byte b : bytes){
                String string = ""
                        + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                        + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                        + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                        + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
                System.out.println(tag+":"+string);
            }

        }
    }
}
