package users.core;

public class User {

	private int id;
	
	private String nickname;

	private String userName;

	private String firstName;

	private String lastName;

	public User() {
	}

	public User(int id, String nickname, String userName, String firstName, String lastName) {
		this.id = id;
		this.nickname = nickname;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getUsername() {
		return userName;
	}

	public void setUsername(String userName) {
		this.userName = userName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
