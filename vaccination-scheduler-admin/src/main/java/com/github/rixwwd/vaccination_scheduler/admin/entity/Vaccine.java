package com.github.rixwwd.vaccination_scheduler.admin.entity;

import java.time.Duration;

public enum Vaccine {

	/**
	 * Pfizer–BioNTech COVID-19 vaccine
	 * 
	 * @see <a href=
	 *      "https://en.wikipedia.org/wiki/Pfizer%E2%80%93BioNTech_COVID-19_vaccine">https://en.wikipedia.org/wiki/Pfizer%E2%80%93BioNTech_COVID-19_vaccine</a>
	 */
	PFIZER("Pfizer", "BNT162b1", 5, 2, 195, Duration.ofDays(21)),

	/**
	 * Moderna COVID-19 vaccine
	 * 
	 * @see <a href=
	 *      "https://en.wikipedia.org/wiki/Moderna_COVID-19_vaccine">https://en.wikipedia.org/wiki/Moderna_COVID-19_vaccine</a>
	 */
	MODERNA("Moderna", "mRNA-1273", 10, 2, 10, Duration.ofDays(28)),

	/**
	 * Oxford–AstraZeneca COVID-19 vaccine
	 * 
	 * @see <a href=
	 *      "https://en.wikipedia.org/wiki/Oxford%E2%80%93AstraZeneca_COVID-19_vaccine">https://en.wikipedia.org/wiki/Oxford%E2%80%93AstraZeneca_COVID-19_vaccine</a>
	 */
	ASTRA_ZENECA("AstraZeneca", "AZD1222", 10, 2, 10, Duration.ofDays(28));

	private final String makerName;
	private final String codeName;
	private final int dosesPerVial;
	private final int numebrOfDoses;
	private final int minimumDistributionVial;
	private final Duration dosesInterval;

	private Vaccine(String makerName, String codeName, int dosesPerVial, int numebrOfDoses,
			int minimumDistributionVial, Duration dosesInterval) {
		this.makerName = makerName;
		this.codeName = codeName;
		this.dosesPerVial = dosesPerVial;
		this.numebrOfDoses = numebrOfDoses;
		this.minimumDistributionVial = minimumDistributionVial;
		this.dosesInterval = dosesInterval;
	}

	/**
	 * メーカー名
	 * 
	 * @return メーカー名
	 */
	public String getMakerName() {
		return makerName;
	}

	/**
	 * コードネーム
	 * 
	 * @return コードネーム
	 */
	public String getCodeName() {
		return codeName;
	}

	/**
	 * 1バイアルあたりの接種回数
	 * 
	 * @return 1バイアルあたりの接種回数
	 */
	public int getDosesPerVial() {
		return dosesPerVial;
	}

	/**
	 * 接種回数
	 * 
	 * @return 接種回数
	 */
	public int getNumebrOfDoses() {
		return numebrOfDoses;
	}

	/**
	 * 最小流通バイアル数(個)
	 * 
	 * @return 最小流通バイアル数(個)
	 */
	public int getMinimumDistributionVial() {
		return minimumDistributionVial;
	}

	/**
	 * 接種間隔(日)
	 * 
	 * @return 接種間隔(日)
	 */
	public Duration getDosesInterval() {
		return dosesInterval;
	}

}
