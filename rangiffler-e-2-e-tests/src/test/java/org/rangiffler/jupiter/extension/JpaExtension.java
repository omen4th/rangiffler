package org.rangiffler.jupiter.extension;

import jakarta.persistence.EntityManagerFactory;
import org.rangiffler.data.jpa.EmfContext;

public class JpaExtension implements AroundAllTestsExtension {

    @Override
    public void afterAllTests() {
        EmfContext.INSTANCE.storedEmf()
                .forEach(EntityManagerFactory::close);
    }
}
