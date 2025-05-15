package org.example.expert.domain.todo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.example.expert.domain.todo.entity.Todo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPQL을 사용한 커스텀 할 일 검색 기능의 구현체
 */
public class TodoRepositoryImpl implements TodoRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	/**
	 * JPQL 동적 쿼리를 사용하여 조건에 맞는 Todo 리스트를 조회
	 *
	 * @param weather   검색할 날씨 조건 (nullable)
	 * @param startDate 수정일 시작 범위 (nullable)
	 * @param endDate   수정일 종료 범위 (nullable)
	 * @return 조건에 해당하는 Todo 리스트
	 */
	@Override
	public List<Todo> searchByConditions(String weather, LocalDateTime startDate, LocalDateTime endDate) {
		StringBuilder jpql = new StringBuilder("SELECT t FROM Todo t WHERE 1=1");

		// 조건에 따라 쿼리문 구성
		if (weather != null && !weather.isBlank()) {
			jpql.append(" AND t.weather = :weather");
		}
		if (startDate != null) {
			jpql.append(" AND t.updatedAt >= :startDate");
		}
		if (endDate != null) {
			jpql.append(" AND t.updatedAt <= :endDate");
		}

		TypedQuery<Todo> query = em.createQuery(jpql.toString(), Todo.class);

		// 파라미터 바인딩
		if (weather != null && !weather.isBlank()) {
			query.setParameter("weather", weather);
		}
		if (startDate != null) {
			query.setParameter("startDate", startDate);
		}
		if (endDate != null) {
			query.setParameter("endDate", endDate);
		}

		return query.getResultList();
	}
}
