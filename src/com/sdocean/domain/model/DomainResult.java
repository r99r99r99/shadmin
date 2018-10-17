package com.sdocean.domain.model;

import java.util.List;

public class DomainResult {

	private DomainModel domain;
	private List<DomainIndicator> domainIndicators;  //关注的参数

	public DomainModel getDomain() {
		return domain;
	}

	public void setDomain(DomainModel domain) {
		this.domain = domain;
	}

	public List<DomainIndicator> getDomainIndicators() {
		return domainIndicators;
	}

	public void setDomainIndicators(List<DomainIndicator> domainIndicators) {
		this.domainIndicators = domainIndicators;
	}

	
}
