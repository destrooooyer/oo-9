package taxi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DESTR on 2016/4/20.
 */
public class map
{
	private point map[][];
	private point map_backup[][];

	/**
	 * requires: x, y的取值范围为[0,79]
	 * modifies: 无
	 * effects: 返回(x,y)是否与上方的点相连
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean is_up_connected(int x, int y)
	{
		return this.map[x][y].up_connected;
	}

	/**
	 * requires: x, y的取值范围为[0,79]
	 * modifies: 无
	 * effects: 返回(x,y)点是否有红绿灯
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean has_light(int x,int y)
	{
		return this.map[x][y].has_light;
	}

	/**
	 * requires: x, y的取值范围为[0,79]
	 * modifies: 无
	 * effects: 返回(x,y)是否与下方的点相连
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean is_down_connected(int x, int y)
	{
		return this.map[x][y].down_connected;
	}

	/**
	 * requires: x, y的取值范围为[0,79]
	 * modifies: 无
	 * effects: 返回(x,y)是否与左边的点相连
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean is_left_connected(int x, int y)
	{
		return this.map[x][y].left_connected;
	}

	/**
	 * requires: x, y的取值范围为[0,79]
	 * modifies: 无
	 * effects: 返回(x,y)是否与右边的点相连
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean is_right_connected(int x, int y)
	{
		return this.map[x][y].right_connected;
	}

	private int traffic[][][];

	/**
	 * requires: x, y的取值范围是[0,79]，direct可取的值有"up", "down", "left", "right"，x, y, direct三个参数确定的边存在
	 * modifies: 无
	 * effects: 返回(x,y)点direct方向上的路的流量
	 *
	 * @param x
	 * @param y
	 * @param direct
	 * @return
	 */
	public int get_traffic(int x, int y, String direct)
	{
		if (x >= 0 && x < 80 &&
				y >= 0 && y < 80)
		{
			for (int i = 0; i < 4; i++)
			{
				if (direct == direction._direction[i])
					return traffic[x][y][i];
			}
			return 2147483640;
		}
		else
		{
			return 2147483640;
		}
	}

	/**
	 * requires: x, y的取值范围是[0,79]，direct可取的值有"up", "down", "left", "right"，x, y, direct三个参数确定的边存在
	 * modifies: 无
	 * effects: (x,y)点direct方向上的路的流量增加1
	 *
	 * @param x
	 * @param y
	 * @param direct
	 * @return
	 */
	public boolean add_traffic(int x, int y, String direct)
	{
		if (x >= 0 && x < 80 &&
				y >= 0 && y < 80)
		{
			for (int i = 0; i < 4; i++)
			{
				if (direct == direction._direction[i])
				{
					traffic[x][y][i]++;

					//对应的也加，这个的上对那个的下
					int[] coor = new int[]{1, 0, 3, 2};
					int[] temp_x = new int[]{x - 1, x + 1, x, x};
					int[] temp_y = new int[]{y, y, y - 1, y + 1};
//					System.out.println(x+" "+y+" "+direct);
					traffic[temp_x[i]][temp_y[i]][coor[i]]++;

					return true;
				}
			}
			return false;
		}
		else
		{
			return false;
		}
	}

	/**
	 * requires: 无
	 * modifies: 无
	 * effects: 将traffic全部清零
	 */
	public void clear_traffic()
	{
		for (int i = 0; i < 80; i++)
			for (int j = 0; j < 80; j++)
				for (int k = 0; k < 4; k++)
					traffic[i][j][k] = 0;
	}

	/**
	 * requires: x, y的取值范围是[0,79], direct为"up", "down", "left", "right"中的一个，输入的边存在
	 * modifies: 无
	 * effects: 摧毁对应的边
	 *
	 * @param x
	 * @param y
	 * @param direct
	 */
	public boolean close_edge(int x, int y, String direct)
	{
		if (x < 0 || x >= 80 ||
				y < 0 || y >= 80)
			return false;

		switch (direct)
		{
			case direction.up:
			{
				if (this.map[x][y].up_connected)
				{
					this.map[x][y].up_connected = false;
					this.map[x - 1][y].down_connected = false;
					return true;
				}
				return false;
			}
			case direction.down:
			{

				if (this.map[x][y].down_connected)
				{
					this.map[x][y].down_connected = false;
					this.map[x + 1][y].up_connected = false;
					return true;
				}
				return false;
			}
			case direction.left:
			{
				if (this.map[x][y].left_connected)
				{
					this.map[x][y].left_connected = false;
					this.map[x][y - 1].right_connected = false;
					return true;
				}
				return false;
			}
			case direction.right:
			{
				if (this.map[x][y].right_connected)
				{
					this.map[x][y].right_connected = false;
					this.map[x][y + 1].left_connected = false;
					return true;
				}
				return false;
			}
			default:
			{
				return false;
			}
		}
	}

	/**
	 * requires: x, y的取值范围是[0,79], direct为"up", "down", "left", "right"中的一个，输入的边不存在且在地图内
	 * modifies: 无
	 * effects: 创建对应的边
	 *
	 * @param x
	 * @param y
	 * @param direct
	 */
	public boolean open_edge(int x, int y, String direct)
	{
		if (x < 0 || x >= 80 ||
				y < 0 || y >= 80)
			return false;

		switch (direct)
		{
			case direction.up:
			{
				if (!this.map[x][y].up_connected && x - 1 >= 0 && map_backup[x][y].up_connected)
				{
					this.map[x][y].up_connected = true;
					this.map[x - 1][y].down_connected = true;
					return true;
				}
				return false;
			}
			case direction.down:
			{

				if (!this.map[x][y].down_connected && x + 1 < 80 && map_backup[x][y].down_connected)
				{
					this.map[x][y].down_connected = true;
					this.map[x + 1][y].up_connected = true;
					return true;
				}
				return false;
			}
			case direction.left:
			{
				if (!this.map[x][y].left_connected && y - 1 >= 0 && map_backup[x][y].left_connected)
				{
					this.map[x][y].left_connected = true;
					this.map[x][y - 1].right_connected = true;
					return true;
				}
				return false;
			}
			case direction.right:
			{
				if (!this.map[x][y].right_connected && y + 1 < 80 && map_backup[x][y].right_connected)
				{
					this.map[x][y].right_connected = true;
					this.map[x][y + 1].left_connected = true;
					return true;
				}
				return false;
			}
			default:
			{
				return false;
			}
		}
	}

	/**
	 * requires: 无
	 * modifies: 无
	 * effects: 初始化map, traffic
	 */
	public map()
	{
		this.map = new point[82][82];
		for (int i = 0; i < 80; i++)
			for (int j = 0; j < 80; j++)
				this.map[i][j] = new point();

		this.map_backup = new point[82][82];
		for (int i = 0; i < 80; i++)
			for (int j = 0; j < 80; j++)
				this.map_backup[i][j] = new point();

		this.traffic = new int[82][82][4];
		clear_traffic();

	}

	/**
	 * requires: path路径存在且为文件，且有权限，内容的数据符合作业要求
	 * modifies: 无
	 * effects: 从地图文件中读取数据，并将数据存入map
	 *
	 * @param path
	 * @return
	 */
	public boolean read_file(String path, String path_2)
	{
		try
		{
			File file_temp = new File(path);
			File file_temp_2 = new File(path_2);
			if (file_temp.exists() && file_temp.isFile() &&
					file_temp_2.exists() && file_temp_2.isFile())
			{

				//读地图信息--------------------------------
				FileReader fr = new FileReader(file_temp);
				BufferedReader br = new BufferedReader(fr);

				String str_temp = new String("");
				for (int i = 0; i < 80; i++)
				{
					str_temp = br.readLine();

					if (str_temp == null)
					{
						System.out.println("文件中内容不符合要求");
						return false;
					}

					String pt = "^[0-9]{80,80}$";
					Matcher mt = Pattern.compile(pt).matcher(str_temp);
					if (!mt.find())
					{
						System.out.println("文件中内容不符合要求");
						return false;
					}
					else
					{
						for (int j = 0; j < 80; j++)
						{
							try
							{
//								System.out.println(this.map[i][j].getX());
								this.map[i][j].x = Integer.parseInt(str_temp.substring(j, j + 1));
							}
							catch (Exception e)
							{
//								e.printStackTrace();
							}
						}
					}
				}

				//读道路交叉信息--------------------------------

				fr = new FileReader(file_temp_2);
				br = new BufferedReader(fr);

				for (int i = 0; i < 80; i++)
				{
					str_temp = br.readLine();

					if (str_temp == null)
					{
						System.out.println("文件中内容不符合要求");
						return false;
					}

					String pt = "^[01]{80,80}$";
					Matcher mt = Pattern.compile(pt).matcher(str_temp);
					if (!mt.find())
					{
						System.out.println("文件中内容不符合要求");
						return false;
					}
					else
					{
						for (int j = 0; j < 80; j++)
						{
							try
							{
//								System.out.println(this.map[i][j].getX());
								this.map[i][j].cross_info = Integer.parseInt(str_temp.substring(j, j + 1));
							}
							catch (Exception e)
							{
//								e.printStackTrace();
							}
						}
					}
				}

				return true;
			}
			else
			{
				System.out.println("文件不存在或是目录");
				return false;
			}
		}
		catch (Exception e)
		{
			System.out.println("请输入正确的参数，读取输入文件失败");
			return false;
		}
	}

	/**
	 * requires: 无
	 * modifies:
	 * effects: 由map中的数据得出地图哪些边存在，哪些边不存在，存入map
	 */
	public void init()
	{
		//初始化图的边
		for (int i = 0; i < 80; i++)
		{
			for (int j = 0; j < 80; j++)
			{
				switch (this.map[i][j].x)
				{
					case 0:
					{
						break;
					}
					case 1:
					{
						if (j + 1 < 80)
						{
							this.map[i][j].right_connected = true;
							this.map[i][j + 1].left_connected = true;
						}
						break;
					}
					case 2:
					{
						if (i + 1 < 80)
						{
							this.map[i][j].down_connected = true;
							this.map[i + 1][j].up_connected = true;
						}
						break;
					}
					case 3:
					{
						if (i + 1 < 80)
						{
							this.map[i + 1][j].up_connected = true;
							this.map[i][j].down_connected = true;
						}
						if (j + 1 < 80)
						{
							this.map[i][j + 1].left_connected = true;
							this.map[i][j].right_connected = true;
						}
						break;
					}
					default:
						break;
				}
			}
		}

		//初始化红绿灯--------------------------------
		for(int i=0;i<80;i++)
		{
			for(int j=0;j<80;j++)
			{
				this.map[i][j].set_light();
			}
		}

		for (int i = 0; i < 80; i++)
		{
			for (int j = 0; j < 80; j++)
				map_backup[i][j] = map[i][j].clone();
		}

	}

	/**
	 * requires: 无
	 * modifies: 无
	 * effects: 输出map的信息，（设计者自行测试用
	 */
	public void print()
	{
		for (int i = 0; i < 80; i++)
		{
			for (int j = 0; j < 80; j++)
			{
				System.out.print(this.map[i][j].x + " ");
			}
			System.out.println();
		}
	}


	class point
	{
		private int x;          //原始值
		private int cross_info; //交叉信息

		//与上下左右是否联通
		private boolean up_connected;
		private boolean down_connected;
		private boolean left_connected;
		private boolean right_connected;

		//是否有红绿灯
		private boolean has_light;

		/**
		 * requires: 无
		 * modifies: 无
		 * effects: 初始化x, up_connected, down_connected, left_connected和right_connected
		 */
		public point()
		{
			this.x = 0;
			this.up_connected = false;
			this.down_connected = false;
			this.left_connected = false;
			this.right_connected = false;
			this.cross_info = 0;
			this.has_light = false;
		}

		/**
		 * requires: 无
		 * modifies: 无
		 * effects: 返回自己的副本
		 *
		 * @return
		 */
		public point clone()
		{
			point temp = new point();
			temp.x = this.x;
			temp.right_connected = this.right_connected;
			temp.left_connected = this.left_connected;
			temp.up_connected = this.up_connected;
			temp.down_connected = this.down_connected;
			return temp;
		}

		/**
		 * requires: 无
		 * modifies: 无
		 * effects: 设置this.has_light
		 */
		public void set_light()
		{
			int flag = 0;
			if (this.up_connected) flag++;
			if (this.down_connected) flag++;
			if (this.left_connected) flag++;
			if (this.right_connected) flag++;
			if (flag >= 3)
				this.has_light = true;
			else
				this.has_light = false;
		}

	}

	public static class direction
	{
		public static final String up = "up";
		public static final String down = "down";
		public static final String left = "left";
		public static final String right = "right";
		public static final String[] _direction = new String[]{"up", "down", "left", "right"};
	}

}
