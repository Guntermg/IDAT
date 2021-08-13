/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Support;

/**
 *
 * @author Gunter M. Gaede
 */
public class ConverteISIHE {
    													//y					i				q
	public double [][]pesoYic=new double[][]{{0.299,0.587,0.114},{0.596,-0.275,-0.321},{0.212,-0.523,0.311}};
	public double [][]pesoRgb=new double[][]{{1,0.956,0.621},{ 1,-0.272,-0.647},{1,-1.106,1.703}};
													//r					g					b

	private double y,i,q;

	public ConverteISIHE(double y , double i, double q) {
		this.setY(y);
		this.setI(i);
		this.setQ(q);
	}
	public ConverteISIHE() {
		this (0,0,0);
	}
	
	public double getY() {
		return this.y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getI() {
		return this.i;
	}

	public void setI(double i) {
		this.i = i;
	}

	public double getQ() {
		return this.q;
	}

	public void setQ(double q) {
		this.q = q;
	}
	
	public void ConverteRGBparaYIQ(int r,int g,int b) {
		double y,i,q;
		y=( (r*this.pesoYic[0][0])+(g*this.pesoYic[0][1])+(b*this.pesoYic[0][2]) );
		i=( (r*this.pesoYic[1][0])+(g*this.pesoYic[2][1])+(b*this.pesoYic[1][2]) );
		q=( (r*this.pesoYic[2][0])+(g*this.pesoYic[2][1])+(b*this.pesoYic[2][2]) );
		this.setY(y);
		this.setI(i);
		this.setQ(q);
	}
	public void ConverteYIQparaRGB(int y, int i, int q) {
		double r,g,b;
		r=( (y*this.pesoRgb[0][0])+(i*this.pesoRgb[0][1])+(q*this.pesoRgb[0][2]) );
		g=( (y*this.pesoRgb[1][0])+(i*this.pesoRgb[1][1])+(q*this.pesoRgb[1][2]) );
		b=( (y*this.pesoRgb[2][0])+(i*this.pesoRgb[2][1])+(q*this.pesoRgb[2][2]) );
		if(r<0)r=0;
		if(g<0)g=0;
		if(b<0)b=0;
		if(r>255)r=255;
		if(g>255)g=255;
		if(b>255)b=255;
		this.setY(r);
		this.setI(g);
		this.setQ(b);
	}

}
