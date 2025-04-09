package com.plasto.api.domain;

import com.plasto.api.config.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Table(name = "files")
@Entity
public class File extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String uuid;

    @Column(nullable = false,unique = true,length = 100)
    String fileName;

    String contentType;

    String folder;

    Long fileSize;

    String extension;

}
