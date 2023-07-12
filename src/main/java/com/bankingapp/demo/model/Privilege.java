package com.bankingapp.demo.model;

//import ccms.hris.hris.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Privilege{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long  id;
    private String name;

    @ManyToMany(mappedBy = "privileges", cascade = CascadeType.ALL)
    private List<Role> roles;

    public Privilege(String name) {
        this.name = name;
    }
}
