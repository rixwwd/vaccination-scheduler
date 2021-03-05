package com.github.rixwwd.vaccination_scheduler.admin.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import javax.validation.groups.Default;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.SmartValidator;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.ColumnType;
import com.github.rixwwd.vaccination_scheduler.admin.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.admin.repository.PublicUserRepository;

@Service
public class PublicUserUploadService {

	private static final Logger logger = LoggerFactory.getLogger(PublicUserUploadService.class);

	private PublicUserRepository publicUserRepository;

	private SmartValidator smartValidator;

	public PublicUserUploadService(PublicUserRepository publicUserRepository, SmartValidator smartValidator) {
		this.publicUserRepository = publicUserRepository;
		this.smartValidator = smartValidator;
	}

	@Transactional(rollbackFor = BindException.class)
	public void createPublicUserFromCsv(InputStream inputStream) throws BindException {

		//@formatter:off
		var schema = CsvSchema.builder()
				.addColumn("loginName", ColumnType.STRING)
				.addColumn("plainPassword", ColumnType.STRING)
				.addColumn("coupon", ColumnType.STRING)
				.addColumn("name", ColumnType.STRING)
				.addColumn("hurigana", ColumnType.STRING)
				.addColumn("birthday", ColumnType.STRING)
				.addColumn("address", ColumnType.STRING)
				.addColumn("telephoneNumber", ColumnType.STRING)
				.addColumn("email", ColumnType.STRING)
				.addColumn("sms", ColumnType.STRING)
				.setUseHeader(false)
				.build();
		//@formatter:on

		var mapper = new CsvMapper();
		try {
			MappingIterator<PublicUser> mi = mapper.readerFor(PublicUser.class).with(schema).readValues(inputStream);
			int lineNumber = 0;
			while (mi.hasNext()) {
				var publicUser = mi.nextValue();

				var result = new BeanPropertyBindingResult(publicUser, "row" + lineNumber);
				smartValidator.validate(publicUser, result, Default.class, PublicUser.CreateFromCSV.class);
				if (result.hasErrors()) {
					if (logger.isDebugEnabled()) {
						logger.debug("CSV Validation error: " + result.toString());
					}
					throw new BindException(result);
				}

				publicUserRepository.save(publicUser);
				lineNumber++;
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}

	}
}
