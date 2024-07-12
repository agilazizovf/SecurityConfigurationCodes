package az.project.securityConfiguration.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "authorities")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    @Id
    private String name;
}
