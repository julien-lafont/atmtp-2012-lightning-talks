package service.auth

import service.auth.providers.Twitter

object ProviderDispatching {

  def get(provider: String) : GenericProvider = {
    new Twitter()
  }
}