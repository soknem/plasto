package com.plasto.api.feature.file;

import com.plasto.api.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long>{

    Optional<File> findByFileName(String fileName);

    boolean existsByFileName(String fileName);

    boolean existsByUuid(String uuid);

    Optional<File> findByUuid(String uuid);


}
