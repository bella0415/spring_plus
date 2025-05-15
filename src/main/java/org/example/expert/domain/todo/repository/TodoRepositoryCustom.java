package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 커스텀 할 일 검색 기능을 위한 Repository 인터페이스
 */
public interface TodoRepositoryCustom {

	/**
	 * weather 조건과 수정일 범위(startDate ~ endDate)를 기준으로 Todo 리스트를 조회
	 *
	 * @param weather   검색할 날씨 조건 (nullable)
	 * @param startDate 수정일 시작 범위 (nullable)
	 * @param endDate   수정일 종료 범위 (nullable)
	 * @return 조건에 해당하는 Todo 리스트
	 */
	List<Todo> searchByConditions(String weather, LocalDateTime startDate, LocalDateTime endDate);
}
