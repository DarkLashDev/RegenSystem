package fr.darklash.regensystem.api.regen;

/**
 * <h1>RegenResult</h1>
 *
 * <p>
 * Represents the result of a zone regeneration attempt.
 * This enum is returned by {@link fr.darklash.regensystem.api.zone.Zone#regenerate()}
 * and indicates how the regeneration process ended.
 * </p>
 *
 * <h2>Lifecycle Meaning</h2>
 *
 * <p>
 * A regeneration attempt can:
 * </p>
 * <ul>
 *   <li>Complete successfully</li>
 *   <li>Be skipped if nothing needs to be regenerated</li>
 *   <li>Be stopped intentionally (cancelled or disabled)</li>
 *   <li>Fail due to an internal error</li>
 * </ul>
 *
 * <h2>Usage Example</h2>
 *
 * <pre>{@code
 * RegenResult result = zone.regenerate();
 *
 * if (result.isSuccess()) {
 *     // Regeneration completed
 * } else if (result.isFinal()) {
 *     // Regeneration was stopped or failed
 * }
 * }</pre>
 *
 * <h2>API Stability</h2>
 *
 * <ul>
 *   <li>Part of the <b>stable public API</b></li>
 *   <li>Backward compatible within the same major version</li>
 * </ul>
 *
 * @since API 1.0.0
 */
public enum RegenResult {

    /**
     * Regeneration completed successfully.
     *
     * <p>
     * All blocks were restored to their original state.
     * </p>
     */
    SUCCESS,

    /**
     * Regeneration was skipped.
     *
     * <p>
     * This usually occurs when:
     * </p>
     * <ul>
     *   <li>The zone is already fully regenerated</li>
     *   <li>No blocks require restoration</li>
     * </ul>
     */
    SKIP,

    /**
     * Regeneration was intentionally stopped.
     *
     * <p>
     * This typically happens when:
     * </p>
     * <ul>
     *   <li>The zone is disabled</li>
     *   <li>{@link fr.darklash.regensystem.api.event.ZonePreRegenEvent} was cancelled</li>
     * </ul>
     */
    STOP,

    /**
     * Regeneration failed due to an internal error.
     *
     * <p>
     * This indicates an unexpected failure such as:
     * </p>
     * <ul>
     *   <li>World or chunk access issues</li>
     *   <li>Data corruption</li>
     *   <li>Unhandled exceptions</li>
     * </ul>
     */
    ERROR;

    /**
     * Checks whether this result represents a successful regeneration.
     *
     * @return {@code true} if the result is {@link #SUCCESS}
     */
    public boolean isSuccess() {
        return this == SUCCESS;
    }

    /**
     * Checks whether this result represents a final state.
     *
     * <p>
     * Final results indicate that the regeneration process
     * should not be retried automatically.
     * </p>
     *
     * @return {@code true} if the result is {@link #STOP} or {@link #ERROR}
     */
    public boolean isFinal() {
        return this == STOP || this == ERROR;
    }
}
