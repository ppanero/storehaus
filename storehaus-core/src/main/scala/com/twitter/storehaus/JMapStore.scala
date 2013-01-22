/*
 * Copyright 2010 Twitter Inc.
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

import com.twitter.util.Future

/**
 *  @author Oscar Boykin
 *  @author Sam Ritchie
 */

abstract class JMapStore[S <: JMapStore[S,K,V],K,V] extends MutableStore[S,K,V] {
  protected val jstore: java.util.Map[K,Option[V]]
  def storeGet(k: K): Option[V] = {
    val stored = jstore.get(k)
    if (stored != null)
      stored
    else
      None
  }
  override def get(k: K): Future[Option[V]] = Future.value(storeGet(k))
  override def multiGet(ks: Set[K]): Future[Map[K,V]] =
    Future.value(Store.zipWith(ks) { storeGet(_) })

  override def -(k: K): Future[S] = {
    jstore.remove(k)
    Future.value(this.asInstanceOf[S])
  }
  override def +(pair: (K,V)): Future[S] = {
    jstore.put(pair._1, Some(pair._2))
    Future.value(this.asInstanceOf[S])
  }
}
