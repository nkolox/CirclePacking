package main;
/**
 * Created by LocalAdm on 23.12.2016.
 */
public class Circles {
    // a "virtuális" négyzet merete
    static final int wp=1000;

        public int n;
        public double r;
        public int tr;

        public int getN() { return n; }
        public void setN(int n) { this.n=n; }
        public int getTr() { return tr; }
        public void setTr(int tr) { this.tr=tr; }
        public Circles(int n, int tr) { this.n=n; this.tr=tr; }
        public double getSurface() {
            if(n==1) { return (n*r*r*Math.PI); }
            else { return (n*r*r*Math.PI)/((wp+2*r)*(wp+2*r)); }
        }
}
