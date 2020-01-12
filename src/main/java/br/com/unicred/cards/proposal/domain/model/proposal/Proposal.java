package br.com.unicred.cards.proposal.domain.model.proposal;

import br.com.unicred.cards.proposal.domain.model.Model;

public class Proposal extends Model {

	private String name;

  private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
