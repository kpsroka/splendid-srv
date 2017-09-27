/*
 * Copyright 2017 K. P. Sroka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.rk.splendid.dao.entities;

import com.googlecode.objectify.annotation.Ignore;
import net.rk.splendid.dto.NoSelection;
import net.rk.splendid.dto.Selection;

enum SelectionType {
  NO_SELECTION
}

abstract class OfySelection {
  @Ignore SelectionType selectionType;

  static Selection toDto(OfySelection ofySelection) {
    if (ofySelection.selectionType.equals(SelectionType.NO_SELECTION)) {
      return new NoSelection();
    } else {
      throw new IllegalArgumentException("Not supported type: " + ofySelection.selectionType);
    }
  }
}
