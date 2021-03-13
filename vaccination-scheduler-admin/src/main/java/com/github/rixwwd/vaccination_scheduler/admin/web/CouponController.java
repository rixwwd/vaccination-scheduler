package com.github.rixwwd.vaccination_scheduler.admin.web;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.rixwwd.vaccination_scheduler.admin.service.CouponService;

@Controller
public class CouponController {

	private CouponService couponService;

	public CouponController(CouponService couponService) {
		this.couponService = couponService;
	}

	@GetMapping("/coupon/upload")
	public ModelAndView upload() {
		return new ModelAndView("coupon/upload");
	}

	@PostMapping("/coupon/upload")
	public ModelAndView upload(@RequestParam("csv") MultipartFile file) throws BindException, IOException {
		couponService.giveCouponFromCsv(file.getInputStream());
		return new ModelAndView("coupon/result");
	}
}
