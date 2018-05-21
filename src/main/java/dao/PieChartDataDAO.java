package dao;

import org.springframework.data.jpa.repository.JpaRepository;

import model.PieChartData;

public interface PieChartDataDAO extends JpaRepository<PieChartData, Integer>{

}
