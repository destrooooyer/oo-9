package taxi;

/**
 * Created by DESTR on 2016/5/13.
 */
public class traffic_light implements Runnable
{
	private int light;
	private int run_flag;
	private int change_flag;

	/**
	 * requires: 无
	 * modifies: 无
	 * effects: 初始化 light, run_flag, change_flag
	 */
	public traffic_light()
	{
		this.light = 0;
		this.run_flag = 1;
		this.change_flag = 0;
	}

	/**
	 * requires: 无
	 * modifies: 无
	 * effects: 返回this.light
	 */
	public int get_light()
	{
		return this.light;
	}

	/**
	 * requires: 无
	 * modifies: 无
	 * effects: 设置this.change_flag为1
	 */
	public void set_change_flag()
	{
		synchronized (this)
		{
			this.change_flag = 1;
		}
	}

	/**
	 * requires: 无
	 * modifies: 无
	 * effects: 返回rep_ok的结果
	 * @return
	 */
	public boolean rep_ok()
	{
		if (this.light >= 0 && this.light <= 1 &&
				this.change_flag >= 0 && this.change_flag <= 1)
			return true;
		else
			return false;
	}

	@Override
	public void run()
	{
		while (this.run_flag == 1)
		{
			//改变信号灯
			synchronized (this)
			{
				if (this.change_flag == 1)
				{
					if (this.light == 1)
						this.light = 0;
					else if (this.light == 0)
						this.light = 1;
					this.change_flag = 0;
				}
			}

			try
			{
				Thread.sleep(5);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
