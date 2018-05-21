package dao;

import org.springframework.data.jpa.repository.JpaRepository;

import model.RingChartData;

public interface RingChartDataDAO extends JpaRepository<RingChartData, Integer>{

}
