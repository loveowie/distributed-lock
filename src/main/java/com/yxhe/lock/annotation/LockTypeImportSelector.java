package com.yxhe.lock.annotation;

import com.yxhe.lock.enums.LockType;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author yxhe
 * @since 2018-12-21 10:55
 */
public abstract class LockTypeImportSelector implements ImportSelector {

    public static final String DEFAULT_LOCK_TYPE_ATTRIBUTE_NAME = "lockType";

    protected String getLockTypeAttributeName() {
        return DEFAULT_LOCK_TYPE_ATTRIBUTE_NAME;
    }

    @Override
    public final String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Class<?> annoType = GenericTypeResolver.resolveTypeArgument(getClass(), LockTypeImportSelector.class);
        assert annoType != null;
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(annoType.getName(), false));
        if (attributes == null) {
            throw new IllegalArgumentException(String.format(
                    "@%s is not present on importing class '%s' as expected",
                    annoType.getSimpleName(), importingClassMetadata.getClassName()));
        }

        LockType lockType = attributes.getEnum(this.getLockTypeAttributeName());
        String[] imports = selectImports(lockType);
        if (imports == null) {
            throw new IllegalArgumentException(String.format("Unknown AdviceMode: '%s'", lockType));
        }
        return imports;
    }

    protected abstract String[] selectImports(LockType lockType);
}
