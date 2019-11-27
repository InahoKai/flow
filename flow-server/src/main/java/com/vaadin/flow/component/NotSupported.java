/*
 * Copyright 2000-2019 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Methods annotated with {@link NotSupported} are mapped to the original
 * webcomponent implementation, but not supported at Java level.
 * <p>
 * Calling methods annotated this way results in no-ops.
 * <p>
 * Subclasses can override the not supported methods and add meaningful
 * implementation to them.
 *
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Documented
public @interface NotSupported {

}
