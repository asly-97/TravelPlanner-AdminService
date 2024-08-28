package com.revature.admin.TravelPlanner.DAOs;

import com.revature.admin.TravelPlanner.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;


@Repository
public interface UserDAO extends JpaRepository<User, UUID> {
}
