package lib.kael;

public class UserConnection
{
	private int     uid;// ƽ̨ID
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	private int 	rid;/* ���ӵ��û�ID*/
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	private int 	x;  /* ���ӵ��û�����X*/
	private int 	y;  /* ���ӵ��û�����Y*/
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	private long  	latency;/* �ͻ��˱���ʱ��ͷ���������ʱ��Ĳ�ֵ */
	//private Channel channel;/* �ͻ�������*/
	public UserConnection()
	{}
	public long getLatency() {
		return latency;
	}
	public void setLatency(long latency) {
		this.latency = latency;
	}
}