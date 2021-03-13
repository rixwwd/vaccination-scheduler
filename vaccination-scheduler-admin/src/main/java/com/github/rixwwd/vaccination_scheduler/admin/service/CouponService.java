package com.github.rixwwd.vaccination_scheduler.admin.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.SmartValidator;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.ColumnType;
import com.github.rixwwd.vaccination_scheduler.admin.entity.Coupon;
import com.github.rixwwd.vaccination_scheduler.admin.repository.CouponRepository;
import com.github.rixwwd.vaccination_scheduler.admin.repository.PublicUserRepository;

@Service
public class CouponService {

	private static final Logger logger = LoggerFactory.getLogger(CouponService.class);

	private SmartValidator smartValidator;

	private CouponRepository couponRepository;

	private PublicUserRepository publicUserRepository;

	public CouponService(SmartValidator smartValidator, CouponRepository couponRepository,
			PublicUserRepository publicUserRepository) {
		this.smartValidator = smartValidator;
		this.couponRepository = couponRepository;
		this.publicUserRepository = publicUserRepository;
	}

	@Transactional(rollbackFor = BindException.class)
	public void giveCouponFromCsv(InputStream inputStream) throws BindException {
		//@formatter:off
		var schema = CsvSchema.builder()
				.addColumn("loginName", ColumnType.STRING)
				.addColumn("coupon", ColumnType.STRING)
				.addColumn("couponName", ColumnType.STRING)
				.setUseHeader(false)
				.build();
		//@formatter:on

		var mapper = new CsvMapper();
		try {
			MappingIterator<GiveCouponCsv> mi = mapper.readerFor(GiveCouponCsv.class).with(schema)
					.readValues(inputStream);
			int lineNumber = 0;
			while (mi.hasNext()) {
				var couponCsv = mi.nextValue();

				var result = new BeanPropertyBindingResult(couponCsv, "row" + lineNumber);
				smartValidator.validate(couponCsv, result);
				if (result.hasErrors()) {
					if (logger.isDebugEnabled()) {
						logger.debug("CSV Validation error: " + result.toString());
					}
					throw new BindException(result);
				}

				// FIXME
				var publicUser = publicUserRepository.findByLoginName(couponCsv.getLoginName()).orElseThrow();
				var coupon = new Coupon();
				coupon.setPublicUserId(publicUser.getId());
				coupon.setCoupon(couponCsv.getCoupon());
				coupon.setName(couponCsv.getCouponName());
				couponRepository.save(coupon);
				lineNumber++;
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} catch (DataIntegrityViolationException e) {
			// FIXME 重複クーポン名の場合
			throw e;
		}
	}
}
