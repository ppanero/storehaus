/*
 * Copyright 2014 Twitter Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twitter.storehaus

import com.twitter.conversions.time._
import com.twitter.util.{JavaTimer, Timer}

import org.scalacheck.Properties

object RetryingStoreProperties extends Properties("RetryingStore") {
  import StoreProperties.storeTest

  implicit val timer: Timer = new JavaTimer(true)

  property("RetryingStore obeys the Store laws, assuming the underlying Store " +
      "always returns results before timeout") =
    storeTest[String, Int] {
      Store.withRetry[String, Int](
        store = new ConcurrentHashMapStore[String, Int](),
        backoffs = for (i <- 0 until 3) yield 1.milliseconds
      )(_ => true)
    }
}
