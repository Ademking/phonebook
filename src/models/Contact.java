package models;

public class Contact {
	int id;
	String firstname;
	String lastname;
	String organization;
	String email;
	String phone;
	String address;
	int created_by;

	public Contact(int id, String firstname, String lastname, String organization, String email, String phone,
			String address, int created_by) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.organization = organization;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.created_by = created_by;
	}

	public int getId() {
		return id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getEmail() {
		return email;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCreatedBy() {
		return created_by;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCreatedBy(int created_by) {
		this.created_by = created_by;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ID: " + this.getId() + "\n" + this.getFirstname() + "\n" + this.getLastname() + "\n" + this.getAddress()
				+ "\n" + this.getPhone() + "\n" + "Created By : " + this.getCreatedBy();

	}

}
