package com.niner;

import java.util.*;
import java.math.*;
import java.io.*;
import java.text.*;

public class bibliophile_6622_wa implements Runnable {
	
	final String taskname = "bibliophile";
	
	public bibliophile_6622_wa(int mod) {
	    this.mod = mod;
	}
	
	int mod = 2239;
	
	void solve() throws IOException
	{
		int N = iread();
		int Y = iread();
		pair[] p = new pair[N];
		for (int i=0; i<N; i++)
			p[i] = new pair(iread(), i+1);
		Arrays.sort(p);
		int[] w = new int[N];
		for (int i=0; i<N; i++)
			w[i] = p[i].w;
		int[][] dyn = new int[N+1][mod];
		int[][] prev = new int[N+1][mod];
		Arrays.fill(prev[0], -1);
		Arrays.fill(dyn[0], -1);
		dyn[0][0] = 0;
		for (int i=0; i<N; i++)
		{
			Arrays.fill(dyn[i+1], -1);
			Arrays.fill(prev[i+1], -1);
			for (int j=0; j<mod; j++)
			{
				if (dyn[i][j]==-1) continue;
				if (dyn[i+1][j]<dyn[i][j])
				{
					dyn[i+1][j] = dyn[i][j];
					prev[i+1][j] = prev[i][j];
				}
				int j1 = (j+w[i])%mod;
				if (dyn[i+1][j1] < dyn[i][j] + w[i] && dyn[i][j]+w[i]<=Y)
				{
					dyn[i+1][j1] = dyn[i][j] + w[i];
					prev[i+1][j1] = i;
				}
			}
		}
		int ans = -1;
		int c = -1;
		for (int k=mod-1; k>=0; k--)
			if (dyn[N][k]<=Y && dyn[N][k]>ans)
			{
				c = k;
				ans = dyn[N][k];
			}
		int[] path = new int[N];
		int cnt = 0, x = N;
		while (prev[x][c]!=-1)
		{
			int c1 = (c + 2 * mod - w[prev[x][c]])%mod;
			x = prev[x][c];
			c = c1;
			path[cnt++] = p[x].num;
		}
		out.write(cnt+"\r\n");
		for (int i=cnt-1; i>=0; i--)
		{
			out.write(path[i]+"");
			if (i>0)
				out.write(" ");
			else out.write("\r\n");
		}
	}
	
	class pair implements Comparable<pair>
	{
		int w, num;
		public pair(int w, int num)
		{
			this.w = w;
			this.num = num;
		}
		@Override
		public int compareTo(pair o) {
			if (w>o.w) return -1;
			if (w<o.w) return -1;
			if (num<o.num) return -1;
			if (num>o.num) return -1;
			return 0;
		}
	}
	
	int iread() throws IOException
	{
		return Integer.parseInt(readword());
	}
	
	long lread() throws IOException
	{
		return Long.parseLong(readword());
	}
	
	double dread() throws IOException
	{
		return Double.parseDouble(readword());
	}

	String readword() throws IOException
	{
		StringBuilder sb = new StringBuilder();
		int c = 0;
		while (c<=32 && c>=0)
			c = in.read();
		if (c<0) return "";
		while (c>32)
		{
			sb.append((char)c);
			c = in.read();
		}
		return sb.toString();
	}
	
	BufferedReader in;
	BufferedWriter out;
	
	@Override
	public void run() {
		try
		{
			in = new BufferedReader(new FileReader(taskname+".in"));
			out = new BufferedWriter(new FileWriter(taskname+".out"));
			solve();
			out.flush();
		} catch (Exception e)
		{
		    throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
//		bibliophile_6622_wa instance = new bibliophile_6622_wa();
//		try
//		{
//			Locale.setDefault(Locale.US);
//		}catch (Exception e)
//		{
//			
//		}
//		new Thread(instance).start();
	}


}
