package com.reply.skillshub.controllers.users;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.reply.skillshub.controllers.users.profileextractor.CertificateExtractorService;
import com.reply.skillshub.controllers.users.profileextractor.ExperienceExtractorService;
import com.reply.skillshub.controllers.users.profileextractor.LanguageExtractorService;
import com.reply.skillshub.controllers.users.profileextractor.SkillExtractorService;
import com.reply.skillshub.controllers.users.profileextractor.UserProfile;
import com.reply.skillshub.data.user.UserService;
import com.reply.skillshub.services.SkillsAgentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserProfileExtractorService {

  private final SkillsAgentService skillsAgentService;

  private final LanguageExtractorService languageExtractorService;
  private final SkillExtractorService skillExtractorService;
  private final CertificateExtractorService certificateExtractorService;
  private final ExperienceExtractorService experienceExtractorService;

  private final UserService userService;

  public void extractInformationFromCvPdf(String userId, MultipartFile cvPdf) {
    var cvInformation = skillsAgentService.extractInformationFromCvPdf(cvPdf);

    var extractedSpeaks = languageExtractorService.handleExtractedLanguages(cvInformation.getLanguages());
    var skills = skillExtractorService.handleExtractedSkills(cvInformation.getSkills());
    var certificates = certificateExtractorService.handleExtractedCertificates(cvInformation.getCertificates());
    var experiences = experienceExtractorService.handleExtractedExperiences(cvInformation.getExperiences());

    var user = userService.findById(userId, UserProfile.class);

    var newLanguages = extractedSpeaks.stream().filter(speak -> {
      var language = speak.getLanguage();
      return user.getSpeaks().stream()
          .noneMatch(userSpeak -> userSpeak.getLanguage().getLanguageName().equals(language.getLanguageName()));
    }).filter(language -> language != null).toList();

    user.getSpeaks().addAll(newLanguages);

    user.getSkills().addAll(skills);
    user.getHasCertificates().addAll(certificates);
    user.getExperiences().addAll(experiences);

    userService.save(user);

  }

}
