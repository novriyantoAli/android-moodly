package net.coblos.moodly.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.coblos.moodly.data.repository.ConsultationRepositoryImpl
import net.coblos.moodly.data.repository.MoodRepositoryImpl
import net.coblos.moodly.data.repository.UserRepositoryImpl
import net.coblos.moodly.domain.repository.ConsultationRepository
import net.coblos.moodly.domain.repository.MoodRepository
import net.coblos.moodly.domain.repository.UserRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindMoodRepository(moodRepositoryImpl: MoodRepositoryImpl): MoodRepository

    @Binds
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindConsultationRepository(consultationRepositoryImpl: ConsultationRepositoryImpl): ConsultationRepository
}
