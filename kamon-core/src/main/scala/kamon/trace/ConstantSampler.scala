package kamon
package trace

import kamon.tag.TagSet
import kamon.trace.SpanContext.SamplingDecision

/**
  * Sampler that always returns the same sampling decision.
  */
class ConstantSampler private(decision: SamplingDecision) extends Sampler {

  override def decide(operationName: String, tags: TagSet): SamplingDecision =
    decision

  override def toString: String =
    s"ConstantSampler(decision = $decision)"
}

object ConstantSampler {

  /** Sampler that always samples requests */
  val Always = new ConstantSampler(SamplingDecision.Sample)

  /** Sampler the never samples requests */
  val Never = new ConstantSampler(SamplingDecision.DoNotSample)
}