package test.aplock;

public class lockimage {

	// private variables
	int _id;
	String _imgname;
	String _realpath;

	// Empty constructor
	public lockimage() {

	}

	// constructor
	public lockimage(int id, String imgname, String realpath) {
		this._id = id;
		this._imgname = imgname;
		this._realpath = _realpath;
	}

	// constructor
	public lockimage(String imgname, String realpath) {
		this._imgname = imgname;
		this._realpath = realpath;
	}

	// getting ID
	public int getID() {
		return this._id;
	}

	// setting id
	public void setID(int id) {
		this._id = id;
	}

	// getting name
	public String getimgName() {
		return this._imgname;
	}

	// setting name
	public void setimgName(String imgname) {
		this._imgname = imgname;
	}

	// getting phone number
	public String getrealpath() {
		return this._realpath;
	}

	// setting phone number
	public void setrealpath(String realpath) {
		this._realpath = realpath;
	}

}
