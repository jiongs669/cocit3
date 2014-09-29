package com.jiongsoft;

import org.junit.Test;

public class LCJSQ {
	@Test
	public void testFA() {
		// int monthFee = 6173;
		// int years = 25;

		int monthFee = 5693;
		int years = 25;
		//
		// int monthFee = 6812;
		// int years = 20;
		//
		// int monthFee = 6348;
		// int years = 20;

		double[] yearGJJs = new double[] { 520 * 12, 520 * 12 + 2500 * 12 };
		double[] rateLCs = new double[] { 0.040, 0.045, 0.048, 0.050 };// 理财年收益率
		double[] rateFZZFs = new double[] { 0.020, 0.025, 0.030 };// 房租半年涨幅

		System.out.println("贷款年限：" + years + "， 月供：" + monthFee + "元");
		System.out.println("理财年收益率 \t\t方案1(卖房还贷) \t\t方案2(卖房理财) \t\t方案3(租房理财)");

		for (double yearGJJ : yearGJJs) {
			for (double rateLC : rateLCs) {

				/*
				 * 1. 方案一: 卖房还贷，将月供转换为基金定投
				 */
				String str1 = "\t\t\t\t";
				double total1 = 0;
				for (int i = 1; i <= years * 12; i++) {

					total1 += monthFee;

					total1 = total1 + total1 * rateLC / 12;
				}

				total1 += (520 + 2500) * 12 * years;
				str1 += new Double(total1).intValue();

				/*
				 * 2. 方案二：月供6173元，卖房款91万 + 公积金理财
				 */
				String str2 = "\t\t\t\t";
				double total2 = 910000;// 房款
				for (int i = 1; i <= years; i++) {

					// 卖房款 + 公积金
					total2 += yearGJJ;

					// 按月计算理财收益
					for (int j = 1; j <= 12; j++) {
						total2 = total2 + total2 * rateLC / 12;
					}
				}

				if (yearGJJ < 30000) {
					total2 += 2500 * 12 * years;// 老婆25年的公积金总额
				}
				str2 += new Double(total2).intValue();

				/*
				 * 3. 方案三：月供6173元，房租费 + 公积金理财
				 */
				String str3 = "\t\t";
				for (double rateFZZF : rateFZZFs) {

					double total3 = 0;
					for (int i = 1; i <= years * 2; i++) {

						// 半年房租费
						int fee = calHarfYearFee(15000, i, rateFZZF);
						// System.out.println("半年租金" + i + "：" + fee + ", 月租金：" + fee / 6);

						total3 += fee;

						// 每年取出住房公积金
						if (i % 2 == 1) {
							total3 += yearGJJ;
						}

						// 半年理财本金收益和
						for (int j = 1; j <= 6; j++) {
							total3 = total3 + total3 * rateLC / 12;
						}
					}

					if (yearGJJ < 30000) {
						total3 += 2500 * 12 * years;// 老婆25年公积金总额
					}

					total3 += 910000;// 房产价值

					str3 += new Double(total3).intValue() + ",";
				}

				System.out.println("\t" + rateLC * 100 + "%" + str1 + str2 + str3);
			}
			System.out.println();
		}
	}

	// 1. 方案一: 卖房还贷，将月供转换为基金定投
	@Test
	public void testFA1() {

		double[] rateLCs = new double[] { 0.040, 0.045, 0.048, 0.050 };// 理财年收益率

		System.out.println("\t\t理财年收益率 \t\t25年资产总额(包括两人25年公积金)");

		for (double rateLC : rateLCs) {

			double total = 0;
			for (int i = 1; i <= 300; i++) {

				// 定存金额累计
				total += 6173;

				// 按月计算理财收益
				total = total + total * rateLC / 12;
			}

			total += 906000;

			System.out.println("\t\t" + rateLC * 100 + "% \t\t\t\t" + new Double(total).intValue());
		}
	}

	// 2. 方案二：月供6173元，卖房款91万 + 公积金理财
	@Test
	public void testFA2() {

		double[] yearGJJs = new double[] { 520 * 12, 520 * 12 + 2500 * 12 };
		double[] rateLCs = new double[] { 0.040, 0.045, 0.048, 0.050 };// 理财年收益率5%

		System.out.println("\t\t理财年收益率 \t\t每年可提取公积金(元) \t\t25年资产总额(元)");

		for (double yearGJJ : yearGJJs) {
			for (double rateLC : rateLCs) {

				double total = 910000;// 房款+公积金理财收益
				for (int i = 1; i <= 25; i++) {

					// 卖房款 + 公积金
					total += yearGJJ;

					// 按月计算理财收益
					for (int j = 1; j <= 12; j++) {
						total = total + total * rateLC / 12;
					}
				}

				if (yearGJJ < 30000) {
					total += 750000;// 老婆25年的公积金总额
				}

				System.out.println("\t\t" + rateLC * 100 + "% \t\t\t\t\t" + yearGJJ + " \t\t\t\t\t" + new Double(total).intValue());
			}
		}
	}

	// 3. 方案三：月供6173元，房租费 + 公积金理财
	@Test
	public void testFA3() {

		// 两个人每年的住房公积金
		// double yearGJJ = 520 * 12 + 2500 * 12;
		double[] yearGJJs = new double[] { 520 * 12, 520 * 12 + 2500 * 12 };
		double[] rateLCs = new double[] { 0.040, 0.045, 0.048, 0.050 };// 理财年收益率5%
		double[] rateFZZFs = new double[] { 0.020, 0.025, 0.030 };// 房租半年涨幅2%

		System.out.println("理财年收益率 \t房租半年涨幅 \t每年可提取公积金(元) \t25年资产总额(元)");

		for (double yearGJJ : yearGJJs) {
			for (double rateLC : rateLCs) {
				for (double rateFZZF : rateFZZFs) {

					double total = 0;// 房租费+理财收益合计
					for (int i = 1; i <= 50; i++) {

						// 半年房租费
						int fee = calHarfYearFee(15000, i, rateFZZF);
						// System.out.println("半年租金" + i + "：" + fee + ", 月租金：" + fee / 6);

						total += fee;

						// 每年取出住房公积金
						if (i % 2 == 1) {
							total += yearGJJ;
							// /System.out.println("理财年收益率：" + rateLC + "，房租半年涨幅：" + rateFZZF + "，每年可提取公积金：" + yearGJJ + "元, 第 " + i / 2 + " 年度累计资产：" + new Double(total).intValue() + "元");
						}

						// 半年理财本金收益和
						for (int j = 1; j <= 6; j++) {
							total = total + total * rateLC / 12;
						}
					}

					if (yearGJJ < 30000) {
						total += 750000;// 老婆25年公积金总额
					}

					total += 910000;// 房产价值

					System.out.println("\t\t" + rateLC * 100 + "%\t\t\t" + rateFZZF * 100 + "%\t\t\t\t\t" + yearGJJ + "\t\t\t\t\t" + new Double(total).intValue());
				}
			}
		}
	}

	/**
	 * 计算第noYear年的租金
	 * 
	 * @param harfYearBase
	 *            半年租金基数
	 * @param noYear
	 *            第几年
	 * @param per
	 * @return
	 */
	int calHarfYearFee(int harfYearFee, int harfYearNO, double per) {
		if (harfYearNO == 1) {
			return harfYearFee;
		}

		return new Double(calHarfYearFee(harfYearFee, harfYearNO - 1, per) * (1 + per)).intValue();
	}
}
