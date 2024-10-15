package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.span.SpanDto;
import com.example.demo.entity.Span;
import com.example.demo.payload.ApiResponse;
import com.example.demo.repository.SpanRepository;

import jakarta.validation.Valid;

@Service 
public class SpanService {

	@Autowired
	SpanRepository spanRepository;
	
	public ApiResponse createSpan(@Valid SpanDto spanDto) {
		
		Span span = new Span();
		
		span.setWhy(spanDto.getWhy());
		span.setHowMuch(spanDto.getHowMuch());
		span.setUsername(spanDto.getUsername());
		span.setDate(spanDto.getDate());
		
		spanRepository.save(span);
		
		return new ApiResponse("Span created", true);
	}

	public ApiResponse editSpan(@Valid Long id, SpanDto spanDto) {

	    Optional<Span> spanOptional = spanRepository.findById(id);

	    if (spanOptional.isPresent()) {
	        Span existingSpan = spanOptional.get();

	        existingSpan.setWhy(spanDto.getWhy());
	        existingSpan.setHowMuch(spanDto.getHowMuch());
	        existingSpan.setUsername(spanDto.getUsername());
	        existingSpan.setDate(spanDto.getDate());
	        spanRepository.save(existingSpan);

	        return new ApiResponse("Span updated", true);
	    } else {
	        return new ApiResponse("Span not found", false);
	    }
	}


	public ApiResponse getAllSpan() {
		List<Span> spanList = spanRepository.findAll();
		return new ApiResponse("Span List", true, spanList);
	}

	public ApiResponse deleteSpan(Long id) {
		Optional<Span> spanOptional = spanRepository.findById(id);
		if (spanOptional.isPresent()) {
			Span span = spanOptional.get();

			spanRepository.delete(span);
			return new ApiResponse("Span deleted", true);
		} else {
			return new ApiResponse("Span not found", false);
		}
	}
}
