package at.c02.aai.app.db.entity;

import java.util.Objects;

public abstract class BaseEntity {

	public abstract Long getId();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (getId() ^ (getId() >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!Objects.equals(this.getClass(), o.getClass())) {
			return false;
		}
		BaseEntity otherEntity = (BaseEntity) o;
		return getId() != null && Objects.equals(getId(), otherEntity.getId());
	}

}
