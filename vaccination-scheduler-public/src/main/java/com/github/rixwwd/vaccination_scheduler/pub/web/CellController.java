package com.github.rixwwd.vaccination_scheduler.pub.web;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.rixwwd.vaccination_scheduler.pub.entity.Cell;
import com.github.rixwwd.vaccination_scheduler.pub.repository.CellRepository;

@Controller
public class CellController {

	private CellRepository cellRepository;

	public CellController(CellRepository cellRepository) {
		this.cellRepository = cellRepository;
	}

	@GetMapping("/api/cells/")
	@ResponseBody
	public List<Cell> cells(@Param("roomId") UUID roomId) {
		var cells = cellRepository.findByRoomId(roomId);
		var cellList = new ArrayList<Cell>();
		for (var cell : cells) {
			cellList.add(cell);
		}
		return cellList;
	}

}
