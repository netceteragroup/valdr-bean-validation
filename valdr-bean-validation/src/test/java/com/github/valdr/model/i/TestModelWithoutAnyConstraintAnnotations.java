package com.github.valdr.model.i;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * https://github.com/netceteragroup/valdr-bean-validation/issues/30 reported issues with this class.
 */
@MappedSuperclass
@XmlRootElement
public class TestModelWithoutAnyConstraintAnnotations {

  @Getter
  @NonNull
  @Column(name = "CREATED_AT")
  @Temporal(TemporalType.TIMESTAMP)
  public Date createdAt;

  @Getter
  @NonNull
  @Column(name = "UPDATED_AT")
  @Temporal(TemporalType.TIMESTAMP)
  public Date updatedAt;

  @Getter
  @JsonIgnore
  @Column(name = "VERSION", columnDefinition = "integer DEFAULT 0", nullable = false)
  @Version
  public int version;

  @PrePersist
  void createdAt() {
    this.createdAt = this.updatedAt = new Date();
  }

  @PreUpdate
  void updatedAt() {
    this.updatedAt = new Date();
  }
}