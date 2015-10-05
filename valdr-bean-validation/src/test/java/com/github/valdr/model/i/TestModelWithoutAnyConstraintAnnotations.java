package com.github.valdr.model.i;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
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