package taxi;

import java.util.Scanner;
import java.util.regex.*;

/**
 * Created by DESTR on 2016/4/20.
 */
public class main
{
	/**
	 * requires: 无
	 * modifies: 无
	 * effects: 输入地图，初始化_map, 创建调度器_disp，为其启动线程，启动测试线程
	 * @param argv
	 */
	public static void main(String argv[])
	{
		map _map = new map();
		String path = new String();
		Scanner sc = new Scanner(System.in);

		System.out.println("请输入地图数据所在的文件的绝对路径");
		if (sc.hasNextLine())
			path = sc.nextLine();
		else
			System.exit(0);

		String path_2=new String();
		System.out.println("请输入道路交叉方式信息所在的文件的绝对路径");
		if (sc.hasNextLine())
			path_2 = sc.nextLine();
		else
			System.exit(0);

		if (!_map.read_file(path,path_2))
		{
			System.out.println("读取文件失败");
			System.exit(0);
		}
		_map.init();
//		_map.print();

		disp _disp = new disp(_map);
		new Thread(_disp).start();





		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		//在此处启动测试线程

		test_thread _test_thread=new test_thread(_disp);    //将_disp作为参数传给测试线程
		new Thread(_test_thread).start();


		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//		//控制台输入请求
//		while (sc.hasNext())
//		{
//			String temp_str = sc.nextLine();
////			if(temp_str.equals("123"))
////			{
////				_disp.close_edge(10,11,"left");
////				_disp.open_edge(10,10,"right");
////				break;
////			}
//			String pt = "^([1-7]?[0-9])\\s+([1-7]?[0-9])\\s+([1-7]?[0-9])\\s+([1-7]?[0-9])$";
//			Matcher mt = Pattern.compile(pt).matcher(temp_str);
//			if (mt.find())
//			{
//				try
//				{
//					int x1 = Integer.parseInt(mt.group(1));
//					int y1 = Integer.parseInt(mt.group(2));
//					int x2 = Integer.parseInt(mt.group(3));
//					int y2 = Integer.parseInt(mt.group(4));
//					_disp.add_request(x1, y1, x2, y2);
//				}
//				catch (Exception e)
//				{
//					System.out.println("输入无效");
//				}
//			}
//			else
//			{
//				System.out.println("输入无效");
//			}
//		}
	}

	/**
	 * requires: 无
	 * modifies: 无
	 * effects: 返回rep_ok的结果
	 * @return
	 */
	public boolean rep_ok()
	{
		return true;
	}
}
