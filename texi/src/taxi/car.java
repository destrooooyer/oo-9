package taxi;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

/**
 * Created by DESTR on 2016/4/20.
 */
public class car
{
	private String status;
	private int reputation;
	private int stop_count;
	private int wait_count;
	private int _x;
	private int _y;
	private int dest_x;
	private int dest_y;
	private int passenger_x;
	private int passenger_y;
	private Vector<Integer> path_x;
	private Vector<Integer> path_y;
	private int path_iter;
	private map _map;
	private boolean stopped2serving;

//	public int getStop_count()
//	{
//		return this.stop_count;
//	}
//
//	public int getWait_count()
//	{
//		return this.wait_count;
//	}

	/**
	 * requires: 无
	 * modifies: 无
	 * effects: 返回 reputation
	 *
	 * @return
	 */
	public int getReputation()
	{
		return this.reputation;
	}

	/**
	 * requires: x1, y1, x2, y2的取值范围是[0,79]
	 * modifies: 无
	 * effects: 将车的status置为"to-passenger",dest_x=x2, dest_y=y2, passenger_x=x1, passenger_y=y1
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void choose(int x1, int y1, int x2, int y2)
	{
		this.status = status_kinds.to_passerger;
		this.dest_x = x2;
		this.dest_y = y2;
		this.passenger_x = x1;
		this.passenger_y = y1;
		//find_shortest_path(_x, _y, x1, y1);
	}

	/**
	 * requires: x=1或3
	 * modifies: 无
	 * effects: 增加reputation相应的数值（由x决定
	 *
	 * @param x
	 */
	public void reputation_up(int x)
	{
		if (x == 1)
			this.reputation += 1;
		else if (x == 3)
			this.reputation += 3;
	}

	/**
	 * requires: 无
	 * modifies: 无
	 * effects: 返回 _x
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
	 * effects: 返回 _y
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
	 * effects: 返回 status
	 *
	 * @return
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * requires: _map是联通的地图
	 * modifies: 无
	 * effects: 初始化 status, reputation, stop_count等变量
	 *
	 * @param _map
	 */
	public car(map _map)
	{
		this.status = status_kinds.waiting;
		this.reputation = 0;
		this.stop_count = 0;
		this.wait_count = 0;
		this._x = new Random().nextInt(80);
		this._y = new Random().nextInt(80);
		this.path_iter = -1;    //path的指针，小于0时做flag：-1:一般/-2:
		this._map = _map;
		this.stopped2serving = false;
		this.path_x = new Vector<Integer>();
		this.path_y = new Vector<Integer>();
	}

	/**
	 * requires: light是0或者1
	 * modifies: 无
	 * effects: 根据车当前的位置，车的状态，地图，以及道路的流量确定车的位置，并将_x, _y更新为新的坐标，
	 * path_x,path_y发生了改变，移动之后，调用了_map的add_traffic，使记录的流量值发生了改变
	 */
	public boolean move(int light)
	{
		switch (this.status)
		{
			case status_kinds.serving:
			{
//				System.out.println(status+" "+_x+" "+_y+" "+this.reputation);
//				if (path_x.size() != 0)
//				{
//					_x = path.get(path_iter).getKey();
//					_y = path.get(path_iter).getValue();
//					path_iter++;
//				}
				if (_x != dest_x || _y != dest_y)
				{
					boolean[] bo_temp = new boolean[4];
					int[] path_len = new int[4];        //记录上下左右四个点为起点的最短路长度
					for (int i = 0; i < 4; i++)
					{
						bo_temp[i] = false;
						path_len[i] = 0;
					}
					int min = 2147000000;

					//四个点分别找最短路，更新min
					if (_map.is_up_connected(_x, _y))
					{
						bo_temp[0] = true;
						find_shortest_path(_x - 1, _y, dest_x, dest_y);
						path_len[0] = path_x.size();
						if (path_x.size() < min)
							min = path_x.size();
					}
					if (_map.is_down_connected(_x, _y))
					{
						bo_temp[1] = true;
						find_shortest_path(_x + 1, _y, dest_x, dest_y);
						path_len[1] = path_x.size();
						if (path_x.size() < min)
							min = path_x.size();
					}
					if (_map.is_left_connected(_x, _y))
					{
						bo_temp[2] = true;
						find_shortest_path(_x, _y - 1, dest_x, dest_y);
						path_len[2] = path_x.size();
						if (path_x.size() < min)
							min = path_x.size();
					}
					if (_map.is_right_connected(_x, _y))
					{
						bo_temp[3] = true;
						find_shortest_path(_x, _y + 1, dest_x, dest_y);
						path_len[3] = path_x.size();
						if (path_x.size() < min)
							min = path_x.size();
					}


					//选出最短的集合
					for (int i = 0; i < 4; i++)
					{
						if (!(bo_temp[i] == true && path_len[i] == min))
							bo_temp[i] = false;
					}

					//min置为最小流量
					min = 2147000000;
					for (int i = 0; i < 4; i++)
					{
						if (bo_temp[i] && _map.get_traffic(_x, _y, map.direction._direction[i]) < min)
							min = _map.get_traffic(_x, _y, map.direction._direction[i]);
					}

					//选出流量最小的集合
					for (int i = 0; i < 4; i++)
					{
						if (bo_temp[i] == true)
							if (_map.get_traffic(_x, _y, map.direction._direction[i]) != min)
								bo_temp[i] = false;
					}

					//从长度最小且流量最小的集合中随机
					int rand = new Random().nextInt(4);
					while (bo_temp[rand] == false)
						rand = new Random().nextInt(4);

					int[] temp_x = new int[]{_x - 1, _x + 1, _x, _x};
					int[] temp_y = new int[]{_y, _y, _y - 1, _y + 1};

					//判断是否需要等红绿灯
					if ((light == 1 &&
							(map.direction._direction[rand] == map.direction.left ||
									map.direction._direction[rand] == map.direction.right)) ||
							(light == 0 &&
									(map.direction._direction[rand] == map.direction.up ||
											map.direction._direction[rand] == map.direction.down)))
					{
						if (this._map.has_light(_x, _y))
							return false;
					}


					//移动过的路径流量++
					_map.add_traffic(_x, _y, map.direction._direction[rand]);

					this._x = temp_x[rand];
					this._y = temp_y[rand];

//					System.out.println("("+this._x+","+this._y+")");

				}
				break;
			}
			case status_kinds.stopped:
			{
//				this.stop_count++;
				break;
			}
			case status_kinds.waiting:
			{
				boolean[] bo_temp = new boolean[4];
				for (int i = 0; i < 4; i++)
					bo_temp[i] = false;

				int[] temp_x = new int[]{_x - 1, _x + 1, _x, _x};
				int[] temp_y = new int[]{_y, _y, _y - 1, _y + 1};

				int min = 2147483600;

				//找流量最小的
				if (_map.is_up_connected(_x, _y))
				{
					bo_temp[0] = true;
					if (_map.get_traffic(_x, _y, map.direction.up) < min)
						min = _map.get_traffic(_x, _y, map.direction.up);
				}
				if (_map.is_down_connected(_x, _y))
				{
					bo_temp[1] = true;
					if (_map.get_traffic(_x, _y, map.direction.down) < min)
						min = _map.get_traffic(_x, _y, map.direction.down);
				}
				if (_map.is_left_connected(_x, _y))
				{
					bo_temp[2] = true;
					if (_map.get_traffic(_x, _y, map.direction.left) < min)
						min = _map.get_traffic(_x, _y, map.direction.left);
				}
				if (_map.is_right_connected(_x, _y))
				{
					bo_temp[3] = true;
					if (_map.get_traffic(_x, _y, map.direction.right) < min)
						min = _map.get_traffic(_x, _y, map.direction.right);
				}

				for (int i = 0; i < 4; i++)
				{
					if (bo_temp[i] == true)
						if (_map.get_traffic(_x, _y, map.direction._direction[i]) != min)
							bo_temp[i] = false;
				}

				int rand = new Random().nextInt(4);
				while (bo_temp[rand] == false)
					rand = new Random().nextInt(4);

				//判断是否需要等红绿灯
				if ((light == 1 &&
						(map.direction._direction[rand] == map.direction.left ||
								map.direction._direction[rand] == map.direction.right)) ||
						(light == 0 &&
								(map.direction._direction[rand] == map.direction.up ||
										map.direction._direction[rand] == map.direction.down)))
				{
					if (this._map.has_light(_x, _y))
						return false;
				}

				//移动过的路径流量++
				_map.add_traffic(_x, _y, map.direction._direction[rand]);

				this._x = temp_x[rand];
				this._y = temp_y[rand];


//				this.wait_count++;
				break;
			}
			case status_kinds.to_passerger:
			{
//				System.out.println(status+" "+_x+" "+_y+" "+this.reputation);
//				if (path_x.size() != 0)
//				{
//					_x = path.get(path_iter).getKey();
//					_y = path.get(path_iter).getValue();
//					path_iter++;
//				}

				if (_x != passenger_x || _y != passenger_y)
				{
					boolean[] bo_temp = new boolean[4];
					int[] path_len = new int[4];
					for (int i = 0; i < 4; i++)
					{
						bo_temp[i] = false;
						path_len[i] = 0;
					}
					int min = 2147000000;

					if (_map.is_up_connected(_x, _y))
					{
						bo_temp[0] = true;
						find_shortest_path(_x - 1, _y, passenger_x, passenger_y);
						path_len[0] = path_x.size();
						if (path_x.size() < min)
							min = path_x.size();
					}
					if (_map.is_down_connected(_x, _y))
					{
						bo_temp[1] = true;
						find_shortest_path(_x + 1, _y, passenger_x, passenger_y);
						path_len[1] = path_x.size();
						if (path_x.size() < min)
							min = path_x.size();
					}
					if (_map.is_left_connected(_x, _y))
					{
						bo_temp[2] = true;
						find_shortest_path(_x, _y - 1, passenger_x, passenger_y);
						path_len[2] = path_x.size();
						if (path_x.size() < min)
							min = path_x.size();
					}
					if (_map.is_right_connected(_x, _y))
					{
						bo_temp[3] = true;
						find_shortest_path(_x, _y + 1, passenger_x, passenger_y);
						path_len[3] = path_x.size();
						if (path_x.size() < min)
							min = path_x.size();
					}

					//选出最短的集合
					for (int i = 0; i < 4; i++)
					{
						if (!(bo_temp[i] == true && path_len[i] == min))
							bo_temp[i] = false;
					}


					min = 2147000000;
					for (int i = 0; i < 4; i++)
					{
						if (bo_temp[i] && _map.get_traffic(_x, _y, map.direction._direction[i]) < min)
							min = _map.get_traffic(_x, _y, map.direction._direction[i]);
					}

					//选出流量最小的
					for (int i = 0; i < 4; i++)
					{
						if (bo_temp[i] == true)
							if (_map.get_traffic(_x, _y, map.direction._direction[i]) != min)
								bo_temp[i] = false;
					}

					int rand = new Random().nextInt(4);
					while (bo_temp[rand] == false)
						rand = new Random().nextInt(4);

					int[] temp_x = new int[]{_x - 1, _x + 1, _x, _x};
					int[] temp_y = new int[]{_y, _y, _y - 1, _y + 1};

					//判断是否需要等红绿灯
					if ((light == 1 &&
							(map.direction._direction[rand] == map.direction.left ||
									map.direction._direction[rand] == map.direction.right)) ||
							(light == 0 &&
									(map.direction._direction[rand] == map.direction.up ||
											map.direction._direction[rand] == map.direction.down)))
					{
						if (this._map.has_light(_x, _y))
							return false;
					}

					//移动过的路径流量++
					_map.add_traffic(_x, _y, map.direction._direction[rand]);

					this._x = temp_x[rand];
					this._y = temp_y[rand];

//					System.out.println("("+this._x+","+this._y+")");

				}

				break;
			}
			default:
		}
		return true;
	}

	/**
	 * requires: 无
	 * modifies: 无
	 * effects: 更新车的status
	 * 如果当前状态是stopped那么stop_count会增加，
	 * 如果当前状态是waitting那么wait_count会增加，
	 * 如果服务完成，那么会调用reputation_up使reputation增加
	 * stopped2serving在状态由stopped变为serving之后会改变成false......
	 */
	public void update_status()
	{
		switch (this.status)
		{
			case status_kinds.serving:
			{
				if (this._x == this.dest_x && this._y == this.dest_y)
				{
					this.stop_count = 0;
					this.status = status_kinds.stopped;
					reputation_up(3);
				}

				break;
			}
			case status_kinds.stopped:
			{
				this.stop_count++;
				if (this.stop_count >= 10)
				{
					this.stop_count = 0;
					if (this.stopped2serving)
					{
						this.status = status_kinds.serving;
//						find_shortest_path(_x, _y, dest_x, dest_y);
						this.stopped2serving = false;
					}
					else
					{
						this.status = status_kinds.waiting;
						this.wait_count = 0;
					}
				}

				break;
			}
			case status_kinds.waiting:
			{
				this.wait_count++;
				if (this.wait_count >= 200)
				{
					this.wait_count = 0;
					this.stop_count = 0;
					this.status = status_kinds.stopped;
				}

				break;
			}
			case status_kinds.to_passerger:
			{
				if (this._x == this.passenger_x && this._y == this.passenger_y)
				{
					this.stop_count = 0;
					this.status = status_kinds.stopped;
					this.stopped2serving = true;
				}

				break;
			}
			default:
		}

	}

	/**
	 * requires: x1, y1, x2, y2的取值范围都是[0,79]
	 * modifies: 无
	 * effects: 找出(x1,y1)与(x2,y2)之间的最短路径，并存入path_x,path_y
	 * path_iter会改变
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	private void find_shortest_path(int x1, int y1, int x2, int y2)
	{
		//起点与终点相同
		if (x1 == x2 && y1 == y2)
		{
			this.path_iter = 0;
			path_x.clear();
			path_y.clear();
			return;
		}

		//记录前驱
		int[][] x_prev = new int[80][80];
		int[][] y_prev = new int[80][80];

		boolean[][] bo = new boolean[80][80];   //记录访问过的点，初始false
		for (boolean[] i : bo)
		{
			for (boolean j : i)
				j = false;
		}

		//队列
		LinkedList<Integer> x_list = new LinkedList<Integer>();
		LinkedList<Integer> y_list = new LinkedList<Integer>();
		x_list.addLast(x1);                 //起点入队
		y_list.addLast(y1);
		bo[x1][y1] = true;                  //起点访问过了

		while (!x_list.isEmpty())
		{
			int x = x_list.removeFirst();   //取出队头
			int y = y_list.removeFirst();

			if (this._map.is_left_connected(x, y) && !bo[x][y - 1])     //与左相连
			{
				x_list.addLast(x);          //左边的点入队
				y_list.addLast(y - 1);
				x_prev[x][y - 1] = x;       //记录其前驱
				y_prev[x][y - 1] = y;
				bo[x][y - 1] = true;        //访问过涂成true

				//新入队的是目的地
				if (x == x2 && y - 1 == y2)
				{
					LinkedList<Integer> temp_list_x = new LinkedList<Integer>();
					LinkedList<Integer> temp_list_y = new LinkedList<Integer>();
					temp_list_x.addLast(x2);
					temp_list_y.addLast(y2);

					int temp_x = x_prev[x2][y2];
					int temp_y = y_prev[x2][y2];
					while (temp_x != x1 || temp_y != y1)
					{
						temp_list_x.addLast(temp_x);
						temp_list_y.addLast(temp_y);
						int temp_temp_x;
						temp_temp_x = x_prev[temp_x][temp_y];
						temp_y = y_prev[temp_x][temp_y];
						temp_x = temp_temp_x;
					}
					this.path_iter = 0;
					path_x.clear();
					path_y.clear();
					while (!temp_list_x.isEmpty())
					{
						this.path_y.add(temp_list_y.removeLast());
						this.path_x.add(temp_list_x.removeLast());
					}
					return;
				}
			}
			if (this._map.is_right_connected(x, y) && !bo[x][y + 1])    //右
			{
				x_list.addLast(x);
				y_list.addLast(y + 1);
				x_prev[x][y + 1] = x;
				y_prev[x][y + 1] = y;
				bo[x][y + 1] = true;

				if (x == x2 && y + 1 == y2)
				{
					LinkedList<Integer> temp_list_x = new LinkedList<Integer>();
					LinkedList<Integer> temp_list_y = new LinkedList<Integer>();
					temp_list_x.addLast(x2);
					temp_list_y.addLast(y2);

					int temp_x = x_prev[x2][y2];
					int temp_y = y_prev[x2][y2];
					while (temp_x != x1 || temp_y != y1)
					{
						temp_list_x.addLast(temp_x);
						temp_list_y.addLast(temp_y);
						int temp_temp_x;
						temp_temp_x = x_prev[temp_x][temp_y];
						temp_y = y_prev[temp_x][temp_y];
						temp_x = temp_temp_x;
					}
					this.path_iter = 0;
					path_x.clear();
					path_y.clear();
					while (!temp_list_x.isEmpty())
					{
						this.path_y.add(temp_list_y.removeLast());
						this.path_x.add(temp_list_x.removeLast());
					}
					return;
				}
			}
			if (this._map.is_down_connected(x, y) && !bo[x + 1][y])     //下
			{
				x_list.addLast(x + 1);
				y_list.addLast(y);
				x_prev[x + 1][y] = x;
				y_prev[x + 1][y] = y;
				bo[x + 1][y] = true;


				if (x + 1 == x2 && y == y2)
				{
					LinkedList<Integer> temp_list_x = new LinkedList<Integer>();
					LinkedList<Integer> temp_list_y = new LinkedList<Integer>();
					temp_list_x.addLast(x2);
					temp_list_y.addLast(y2);

					int temp_x = x_prev[x2][y2];
					int temp_y = y_prev[x2][y2];
					while (temp_x != x1 || temp_y != y1)
					{
//						System.out.println(temp_x+"\t"+temp_y);
						temp_list_x.addLast(temp_x);
						temp_list_y.addLast(temp_y);
						int temp_temp_x;
						temp_temp_x = x_prev[temp_x][temp_y];
						temp_y = y_prev[temp_x][temp_y];
						temp_x = temp_temp_x;
					}
					this.path_iter = 0;
					path_x.clear();
					path_y.clear();
					while (!temp_list_x.isEmpty())
					{
						this.path_y.add(temp_list_y.removeLast());
						this.path_x.add(temp_list_x.removeLast());
					}
					return;
				}
			}
			if (this._map.is_up_connected(x, y) && !bo[x - 1][y])       //上
			{
				x_list.addLast(x - 1);
				y_list.addLast(y);
				x_prev[x - 1][y] = x;
				y_prev[x - 1][y] = y;
				bo[x - 1][y] = true;

				if (x - 1 == x2 && y == y2)
				{
					LinkedList<Integer> temp_list_x = new LinkedList<Integer>();
					LinkedList<Integer> temp_list_y = new LinkedList<Integer>();
					temp_list_x.addLast(x2);
					temp_list_y.addLast(y2);

					int temp_x = x_prev[x2][y2];
					int temp_y = y_prev[x2][y2];
					while (temp_x != x1 || temp_y != y1)
					{
						temp_list_x.addLast(temp_x);
						temp_list_y.addLast(temp_y);
						int temp_temp_x;
						temp_temp_x = x_prev[temp_x][temp_y];
						temp_y = y_prev[temp_x][temp_y];
						temp_x = temp_temp_x;
					}
					this.path_iter = 0;
					path_x.clear();
					path_y.clear();
					while (!temp_list_x.isEmpty())
					{
						this.path_y.add(temp_list_y.removeLast());
						this.path_x.add(temp_list_x.removeLast());
					}
					return;
				}
			}
		}
	}

	class status_kinds
	{
		public static final String waiting = "waiting";
		public static final String stopped = "stopped";
		public static final String serving = "serving";
		public static final String to_passerger = "to-passenger";
	}

}

