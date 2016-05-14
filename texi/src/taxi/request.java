package taxi;

/**
 * Created by DESTR on 2016/4/20.
 */
public class request implements Runnable
{
	private int _x;
	private int _y;
	private int dest_x;
	private int dest_y;
	private disp _disp;
	private boolean[] bo_car;

	/**
	 * requires: x的取值范围是[0,99]
	 * modifies: 无
	 * effects: 返回x号车是否抢过单
	 *
	 * @param x
	 * @return
	 */
	public boolean getBo_car(int x)
	{
		return bo_car[x];
	}

	/**
	 * requires: x的取值范围是[0,99]
	 * modifies: 无
	 * effects: 设定x号车抢过单
	 *
	 * @param x
	 * @return
	 */
	public void setBo_car(int x)
	{
		this.bo_car[x] = true;
	}

	/**
	 * requires: 无
	 * modifies: 无
	 * effects: 返回发出请求坐标的行号
	 *
	 * @return
	 */
	public int get_x()
	{
		return _x;
	}

	/**
	 * requires: 无
	 * modifies: 无
	 * effects: 返回发出请求坐标的列号
	 *
	 * @return
	 */
	public int get_y()
	{
		return _y;
	}

	/**
	 * requires: 无
	 * modifies: 无
	 * effects: 返回请求目的地坐标的行号
	 *
	 * @return
	 */
	public int getDest_x()
	{
		return dest_x;
	}

	/**
	 * requires: 无
	 * modifies: 无
	 * effects: 返回请求目的地坐标的列号
	 *
	 * @return
	 */
	public int getDest_y()
	{
		return dest_y;
	}

	/**
	 * requires: _x, _y, dest_x, dest_y的取值范围为[0,79], _disp是main中创建的调度器
	 * modifies: 无
	 * effects: 对各个变量进行初始化
	 *
	 * @param _x
	 * @param _y
	 * @param dest_x
	 * @param dest_y
	 * @param _disp
	 */
	public request(int _x, int _y, int dest_x, int dest_y, disp _disp)
	{
		this._x = _x;
		this._y = _y;
		this.dest_x = dest_x;
		this.dest_y = dest_y;
		this.bo_car = new boolean[100];
		for (int i = 0; i < 100; i++)
			this.bo_car[i] = false;
		this._disp = _disp;
	}

	/**
	 * requires: 无
	 * modifies: 无
	 * effects: 返回rep_ok的结果
	 * @return
	 */
	public boolean rep_ok()
	{
		if (this._x >= 0 && this._x <= 79 &&
				this._y >= 0 && this._y <= 79 &&
				this.dest_x >= 0 && this.dest_x <= 79 &&
				this.dest_y >= 0 && this.dest_y <= 79 &&
				this._disp.rep_ok())
			return true;
		else
			return false;
	}

	@Override
	public void run()
	{
		try
		{
			Thread.sleep(3000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		_disp.complete_req(this);
	}
}
