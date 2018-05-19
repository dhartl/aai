package at.c02.aai.app.web.api.out;

public class SpecialityOutDTO {
	private Long id;
	private String name;

	public SpecialityOutDTO() {
		super();
	}

	public SpecialityOutDTO(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "InsuranceOutDTO [id=" + id + ", name=" + name + "]";
	}

}
