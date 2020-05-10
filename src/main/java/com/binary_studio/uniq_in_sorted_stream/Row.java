package com.binary_studio.uniq_in_sorted_stream;

import java.util.Objects;

//You CAN modify this class
public final class Row<RowData> {

	private final Long id;

	public Row(Long id) {
		this.id = id;
	}

	public Long getPrimaryId() {
		return this.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Row other = (Row) obj;
		return Objects.equals(id, other.id);
	}

}
