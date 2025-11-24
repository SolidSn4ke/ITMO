package com.example.back.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "import_history")
public class ImportHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean successful;

    @Column(name = "num_of_entities_imported")
    private Long numOfEntitiesImported;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public Long getNumOfEntitiesImported() {
        return numOfEntitiesImported;
    }

    public void setNumOfEntitiesImported(Long numOfEntitiesImported) {
        this.numOfEntitiesImported = numOfEntitiesImported;
    }
}
