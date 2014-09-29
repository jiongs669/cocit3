{
	bizEngine : {// 业务引擎：SFT实现
		type : "com.kmetop.demsy.comlib.impl.engine.SFTBizEngine",
		fields: {
			pkgs: ["com.kmetop.demsy.comlib.impl.base.biz"
					, "com.kmetop.demsy.comlib.impl.sft.system"
					, "com.kmetop.demsy.comlib.impl.base.lib"
					, "com.kmetop.demsy.comlib.impl.base.log"
					, "com.kmetop.demsy.comlib.impl.base.security"
					, "com.kmetop.demsy.comlib.impl.base.web"
				]
		}
	}
}