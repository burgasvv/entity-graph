package org.burgas.entitygraph.mapper;

import org.burgas.entitygraph.dto.Request;
import org.burgas.entitygraph.dto.Response;
import org.burgas.entitygraph.entity.AbstractEntity;
import org.burgas.entitygraph.exception.EntityFieldsEmptyException;
import org.springframework.stereotype.Component;

@Component
public interface EntityMapper<T extends Request, S extends AbstractEntity, V extends Response> {

    default <D> D handleData(D requestData, D entityData) {
        return requestData == null || requestData == "" ? entityData : requestData;
    }

    default <D> D handleDataThrowable(D requestData, String message) {
        if (requestData == null || requestData == "")
            throw new EntityFieldsEmptyException(message);

        return requestData;
    }

    S toEntity(T t);

    V toResponse(S s);
}
