public class WeightedUF
{
	private int[] id;
	private int[] sz;
	public FindUF(int N)
	{
		id = new int[N];
		sz = new int[N];
		for (int i=0; i<N; i++)
		{
			id[i]=i;
			sz[i]=1;
		}
	}
	
	private int root(int n)
	{
		
		while(n != id[n])
		{
			id[n] = id[id[n]];
			n=id[n];
		}
		return n;
	}
	
	public boolean connected(int p, int q)
	{
		return root(p) == root(q);
	}
	public void union(int p, int q)
	{
		if (connected(p,q))return;
		int root_p = root(p);
		int root_q = root(q);
		if (sz[root_p]>=sz[root_q]){id[root_q] = root_p;sz[root_p]+=sz[root_q];}
		else {id[root_p] = root_q;sz[root_q]+=sz[root_p];}
	}
	
	public boolean checkFullConnection()
	{
		for (int i=0;i<N-1;i++)
			if (!connected(i,i+1))return false;
		return true;
	}
}

public static main(String[] args)
{
	int N = StdIn.readInt()
	WeightedUF uf = new WeightedUF(N);
	while(!StdIn.isEmpty())
	{
		int p = StdIn.readInt();
		int q = StdIn.readInt();
		uf.union(p,q);
		if(uf.checkFullConnection()) return;
	}
}