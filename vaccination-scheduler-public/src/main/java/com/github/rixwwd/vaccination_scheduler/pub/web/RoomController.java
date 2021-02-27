package com.github.rixwwd.vaccination_scheduler.pub.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.rixwwd.vaccination_scheduler.pub.entity.Room;
import com.github.rixwwd.vaccination_scheduler.pub.repository.RoomRepository;

@Controller
public class RoomController {

	private RoomRepository roomRepository;

	public RoomController(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	@GetMapping("/api/rooms/")
	@ResponseBody
	public List<Room> rooms() {
		var rooms = roomRepository.findAll();
		var roomList = new ArrayList<Room>();
		for (var room : rooms) {
			roomList.add(room);
		}
		return roomList;
	}
}
