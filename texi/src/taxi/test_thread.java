package taxi;

import java.util.Random;

/**
 * Created by DESTR on 2016/4/23.
 */
public class test_thread implements Runnable
{
	private disp _disp;

	/**
	 * requires: _disp为main函数传来的在其中创建的_disp（disp的对象）
	 * modifies: 无
	 * effects: _disp置为_disp
	 * @param _disp
	 */
	test_thread(disp _disp)
	{
		this._disp = _disp;
	}

	/**
	 * requires:
	 * modifies:
	 * effects: 测试线程，由测试者自定义
	 */
	@Override
	public void run()
	{
		//添加300个请求
		for (int i = 0; i < 300; i++)
		{
			_disp.add_request(new Random().nextInt(80), new Random().nextInt(80), new Random().nextInt(80), new Random().nextInt(80));
		}

		try
		{
			Thread.sleep(3500);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		//输出所有车的状态
		for (int i = 0; i < 100; i++)
		{
			System.out.print("车" + i + ":\t");
			System.out.print("坐标:\t(" + _disp.watcher.get_x(i) + "," + _disp.watcher.get_y(i) + ")\t");
			System.out.print("信誉:\t" + _disp.watcher.get_reputation(i) + "\t");
			System.out.println("状态:\t" + _disp.watcher.get_status(i));
		}


		try
		{
			Thread.sleep(10000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		//输出所有车的状态
		for (int i = 0; i < 100; i++)
		{
			System.out.print("车" + i + ":\t");
			System.out.print("坐标:\t(" + _disp.watcher.get_x(i) + "," + _disp.watcher.get_y(i) + ")\t");
			System.out.print("信誉:\t" + _disp.watcher.get_reputation(i) + "\t");
			System.out.println("状态:\t" + _disp.watcher.get_status(i));
		}
	}
}
