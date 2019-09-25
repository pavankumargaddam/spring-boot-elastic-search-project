package org.pk.com.repository.search;

import org.pk.com.domain.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Elastic search repository
 */
public interface EmployeeSearchRepository extends ElasticsearchRepository<Employee, Long> {

}
