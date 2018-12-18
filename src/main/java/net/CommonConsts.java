package net;

public class CommonConsts {

	public static final String CRLF = "\r\n";
	public static final String CR = "\r";
	public static final String AND = "&";
	public static final String LF = "\n";
	
	//public static final String FILE_SEPARATOR = File.separator;

	public static final String OS_NAME = System.getProperty("os.name"); // Operating system name   
	public static final String OS_ARCH = System.getProperty("os.arch"); // Operating system architecture   
	public static final String OS_VERSION = System.getProperty("os.version"); // Operating system version   
	public static final String USER_NAME = System.getProperty("user.name"); // User's account name   
	public static final String USER_HOME = System.getProperty("user.home"); // User's home directory   
	public static final String USER_DIR = System.getProperty("user.dir"); // User's current working directory 
	public static final String FILE_SEPARATOR = System.getProperty("file.separator"); // File separator ("/" on UNIX)   
	public static final String PATH_SEPARATOR = System.getProperty("path.separator"); // Path separator (":" on UNIX) 
	// 平台独立的换行符，功能和"\n"是一致的,但是此种写法屏蔽了 Windows和Linux的区别 ，更保险一些
	public static final String LINE_SEPARATOR = System.getProperty("line.separator"); // Line separator ("\n" on UNIX)   
	
//	public static void main(String[] args) {
//		   System.out.println("java.version:" + System.getProperty("java.version"));
//		   System.out.println("java.vendor:" + System.getProperty("java.vendor"));
//		   System.out.println("java.vendor.url:" + System.getProperty("java.vendor.url"));
//		   System.out.println("java.home:" + System.getProperty("java.home"));
//		   System.out.println("java.vm.specification.version:" + System.getProperty("java.vm.specification.version"));
//		   System.out.println("java.vm.specification.vendor:" + System.getProperty("java.vm.specification.vendor"));
//		   System.out.println("java.vm.specification.name:" + System.getProperty("java.vm.specification.name"));
//		   System.out.println("java.vm.version:" + System.getProperty("java.vm.version"));
//		   System.out.println("java.vm.vendor:" + System.getProperty("java.vm.vendor"));
//		   System.out.println("java.vm.name:" + System.getProperty("java.vm.name"));
//		   System.out.println("java.specification.version:" + System.getProperty("java.specification.version"));
//		   System.out.println("java.specification.vendor:" + System.getProperty("java.specification.vendor"));
//		   System.out.println("java.specification.name:" + System.getProperty("java.specification.name"));
//		   System.out.println("java.class.version:" + System.getProperty("java.class.version"));
//		   System.out.println("java.class.path:" + System.getProperty("java.class.path"));
//		   System.out.println("java.library.path:" + System.getProperty("java.library.path"));
//		   System.out.println("java.io.tmpdir:" + System.getProperty("java.io.tmpdir"));
//		   System.out.println("java.compiler:" + System.getProperty("java.compiler"));
//		   System.out.println("java.ext.dirs:" + System.getProperty("java.ext.dirs"));
//		   System.out.println("os.name:" + System.getProperty("os.name"));
//		   System.out.println("os.arch:" + System.getProperty("os.arch"));
//		   System.out.println("os.version:" + System.getProperty("os.version"));
//		   System.out.println("file.separator:" + System.getProperty("file.separator"));
//		   System.out.println("line.separator:" + System.getProperty("line.separator"));
//		   System.out.println("user.name:" + System.getProperty("user.name"));
//		   System.out.println("user.home:" + System.getProperty("user.home"));
//		   System.out.println("user.dir:" + System.getProperty("user.dir"));
//		}
}
