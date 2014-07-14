package test;

/*
 * The White Agency Java Coding Question - 2014
 * Author: Gavin Mach 13/07/14
 */
public class Maze {
	
	private int[][] maze = new int[][]{
			{1,0,0,1,1,0},
			{1,0,0,0,1,1},
	        {0,0,1,1,0,1},
	        {1,0,1,0,0,0}
			};
	private int rows, cols;
	private int[][][] visited;
	
	private class Propagate {
		boolean isEnclosed;
		double area;

		public Propagate(boolean isEnclosed, double area) {
			this.isEnclosed = isEnclosed;
			this.area = area;
		}
	}

	public Maze() {
		rows = maze.length;
		cols = maze[0].length;
		visited = new int[rows][cols][2];
		for (int i = 0; i < rows; i++) {
			int[][] a2 = new int[cols][2];
			for (int j = 0; j < cols; j++) {
				int[] a3 = new int[2];
				for (int k = 0; k < 2; k++) {
					a3[k] = 0;
				}
				a2[j] = a3;
			}
			visited[i] = a2;
		}
	}
	
	private boolean can_go_up(int row, int col) {
		return row > 0;
	}
	
	private boolean can_go_left(int row, int col) {
		return col > 0;
	}
	
	private boolean can_go_down(int row, int col) {
		return row < rows-1;
	}
	
	private boolean can_go_right(int row, int col) {
		return col < cols-1;
	}

	private Propagate propagate(int i,int j,int k,int z, boolean was_enclosed) {
		z += 1;
		double area = 0;
		boolean vis = visited[i][j][k] > 0;
		if (vis) {
			return new Propagate(was_enclosed,0);
		}
		visited[i][j][k] = 1;
		area += 0.5;
		boolean enclosed = was_enclosed;
		
		
		if ((k == 0 && maze[i][j] == 0) || (k == 0 && maze[i][j] == 1)) {
			if (can_go_up(i,j)) {
				Propagate prop = propagate(i-1,j,1,z,enclosed);
				area += prop.area;
				if (!prop.isEnclosed) {
					enclosed = false;
				}
			} else {
				enclosed = false;
			}
		}
		if ((k == 0 && maze[i][j] == 0) || (k == 1 && maze[i][j] == 1)) {
			if (can_go_left(i,j)) {
				if (maze[i][j-1] == 0) {
					Propagate prop = propagate(i,j-1,1,z,enclosed);
					area += prop.area;
					if (!prop.isEnclosed) {
						enclosed = false;
					}
				} else {
					Propagate prop = propagate(i,j-1,0,z,enclosed);
					area += prop.area;
					if (!prop.isEnclosed) {
						enclosed = false;
					}
				}
			} else {
				enclosed = false;
			}		
		}
		if ((k == 0 && maze[i][j] == 1) || (k == 1 && maze[i][j] == 0)) {
			if (can_go_right(i,j)) {
				if (maze[i][j+1] == 0) {
					Propagate prop = propagate(i,j+1,0,z,enclosed);
					area += prop.area;
					if (!prop.isEnclosed) {
						enclosed = false;
					}
				} else {
					Propagate prop = propagate(i,j+1,1,z,enclosed);
					area += prop.area;
					if (!prop.isEnclosed) {
						enclosed = false;
					}
				}
			} else {
				enclosed = false;
			}
		}
		if ((k == 1 && maze[i][j] == 1) || (k == 1 && maze[i][j] == 0)) {
			if (can_go_down(i,j)) {
				Propagate prop = propagate(i+1,j,0,z,enclosed);
				area += prop.area;
				if (!prop.isEnclosed) {
					enclosed = false;
				}
			} else {
				enclosed = false;
			}
		}
		z -= 1;
		return new Propagate(enclosed, area);
	}

	public void run() {
		int num_areas = 0;
		double max_area = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				for (int k = 0; k < 2; k++) {
					boolean vis = visited[i][j][k] > 0;
					if (!vis) {
						Propagate prop = propagate(i,j,k,0,true);
						if (prop.isEnclosed) {
							if (prop.area > max_area) {
								max_area = prop.area;
							}
							num_areas += 1;
						}
					}
				}
			}
		}
		
		System.out.println("enclosed areas: " + num_areas);
		System.out.println("max area: " + max_area);
	}
	
	public static void main(String[] args) {
		new Maze().run();
	}
}


